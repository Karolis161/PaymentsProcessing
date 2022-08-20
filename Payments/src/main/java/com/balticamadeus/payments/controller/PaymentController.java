package com.balticamadeus.payments.controller;

import com.balticamadeus.payments.entities.CancellationDetails;
import com.balticamadeus.payments.entities.Notification;
import com.balticamadeus.payments.entities.Payment;
import com.balticamadeus.payments.repository.NotificationsRepository;
import com.balticamadeus.payments.repository.PaymentRepository;
import com.balticamadeus.payments.service.PaymentService;
import com.balticamadeus.payments.validation.FirstValidation;
import com.balticamadeus.payments.validation.SecondValidation;
import com.balticamadeus.payments.validation.ThirdValidation;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.net.InetAddress;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

@RestController
@RequestMapping("api/admin/payment")
public class PaymentController {

    @Autowired
    private ApplicationEventPublisher publisher;
    private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;
    private final NotificationsRepository notificationsRepository;

    @Autowired
    public PaymentController(PaymentService paymentService, PaymentRepository paymentRepository, NotificationsRepository notificationsRepository) {
        this.paymentService = paymentService;
        this.paymentRepository = paymentRepository;
        this.notificationsRepository = notificationsRepository;
    }

    @PostMapping("createType1")
    public void createType1Payment(@Validated(FirstValidation.class) @RequestBody Payment payment) {
        paymentService.createType1Payment(payment);
    }

    @PostMapping("createType2")
    public void createType2Payment(@Validated(SecondValidation.class) @RequestBody Payment payment) {
        paymentService.createType2Payment(payment);
    }

    @PostMapping("createType3")
    public void createType3Payment(@Validated(ThirdValidation.class) @RequestBody Payment payment) {
        paymentService.createType3Payment(payment);
    }

    @PatchMapping("cancel")
    public void cancelPayment(@RequestParam int id) {
        paymentService.cancelPayment(id);
    }

    @GetMapping("valid")
    public List<Integer> getValidPayments(@RequestParam String paymentType) {
        return paymentService.getValidPayments(paymentType);
    }

    @GetMapping("specific")
    public CancellationDetails getSpecificPayment(@RequestParam int id) {
        var pay = paymentService.getSpecificPayment(id);
        return pay;
    }

    @GetMapping("data")
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @GetMapping("notificationDetails")
    public List<Notification> getNotificationDetails() {
        return notificationsRepository.findAll();
    }

    @GetMapping("country")
    public void getCountry() {
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        String ip = req.getRemoteAddr();
        File database = new File("GeoLite2-City.mmdb");
        Logger logger = Logger.getLogger(getClass().getName());

        try {
            DatabaseReader dbReader = new DatabaseReader.Builder(database).build();
            CityResponse response = dbReader.city(InetAddress.getByName(ip));

            FileHandler handler = new FileHandler("ip.log", true);
            logger.addHandler(handler);
            SimpleFormatter formatter = new SimpleFormatter();
            handler.setFormatter(formatter);

            logger.info("IP: " + ip);
            logger.info("Country: " + response.getCountry().getName());
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No country has been found with " + ip + " IP address", e);
        }
    }
}
