package com.balticamadeus.payments.service;

import com.balticamadeus.payments.entities.CancellationDetails;
import com.balticamadeus.payments.entities.Payment;
import com.balticamadeus.payments.notification.PaymentEvent;
import com.balticamadeus.payments.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    private final ApplicationEventPublisher publisher;
    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, ApplicationEventPublisher publisher) {
        this.paymentRepository = paymentRepository;
        this.publisher = publisher;
    }

    public void createType1Payment(Payment payment) {
        payment.setPaymentCurrency("EUR");
        payment.setPaymentType("TYPE1");
        payment.setPaymentValidity("Valid");
        payment.setPaymentDate(LocalDateTime.now());
        paymentRepository.save(payment);

        PaymentEvent paymentEvent = new PaymentEvent(payment.getId());
        publisher.publishEvent(paymentEvent);
    }

    public void createType2Payment(Payment payment) {
        payment.setPaymentCurrency("USD");
        payment.setPaymentType("TYPE2");
        payment.setPaymentValidity("Valid");
        payment.setPaymentDate(LocalDateTime.now());
        paymentRepository.save(payment);

        PaymentEvent paymentEvent = new PaymentEvent(payment.getId());
        publisher.publishEvent(paymentEvent);
    }

    public void createType3Payment(Payment payment) {
        payment.setPaymentType("TYPE3");
        payment.setPaymentValidity("Valid");
        payment.setPaymentDate(LocalDateTime.now());
        paymentRepository.save(payment);

        PaymentEvent paymentEvent = new PaymentEvent(payment.getId());
        publisher.publishEvent(paymentEvent);
    }

    public void cancelPayment(int id) {
        String str = paymentRepository.findPaymentDateById(id).getPaymentDate().getYear()
                + "-" + paymentRepository.findPaymentDateById(id).getPaymentDate().getMonthValue()
                + "-" + paymentRepository.findPaymentDateById(id).getPaymentDate().getDayOfMonth()
                + " 00:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d HH:mm");
        LocalDateTime deadline = LocalDateTime.parse(str, formatter);

        double hoursInSystem = ChronoUnit.HOURS.between(paymentRepository.findPaymentDateById(id).getPaymentDate(), LocalDateTime.now());
        double coefficient;
        if (paymentRepository.findPaymentDateById(id).getPaymentType().equals("TYPE1")) {
            coefficient = 0.05;
        } else if (paymentRepository.findPaymentDateById(id).getPaymentType().equals("TYPE2")) {
            coefficient = 0.1;
        } else {
            coefficient = 0.15;
        }

        double cancellationFee = hoursInSystem * coefficient;

        if (ChronoUnit.MINUTES.between(paymentRepository.findPaymentDateById(id).getPaymentDate(), deadline) < 0) {
            paymentRepository.findPaymentDateById(id).setPaymentValidity("Invalid");
            paymentRepository.findPaymentDateById(id).setPaymentCancellationFee(cancellationFee);
            paymentRepository.save(paymentRepository.findPaymentDateById(id));
        } else {
            throw new IllegalArgumentException("Payment cannot be cancelled");
        }
    }

    public List<Integer> getValidPayments(String paymentType) {
        return paymentRepository.findByPaymentValidityAndPaymentType("Valid", paymentType)
                .stream()
                .map(p -> p.getId())
                .collect(Collectors.toList());
    }

    public CancellationDetails getSpecificPayment(int id) {
        var pay = paymentRepository.findById(id);
        var cancellationDetails = new CancellationDetails(pay.get().getId(), pay.get().getPaymentCancellationFee());
        return cancellationDetails;
    }
}
