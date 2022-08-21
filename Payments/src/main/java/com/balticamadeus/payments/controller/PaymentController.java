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
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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

    private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;
    private final NotificationsRepository notificationsRepository;
    private static final String LOG_NAME = "ip.log";
    private static final String PAYMENT_NAME = "Payment";
    private static final String PAYMENT_CREATION = " has been created";

    @Autowired
    public PaymentController(PaymentService paymentService, PaymentRepository paymentRepository, NotificationsRepository notificationsRepository) {
        this.paymentService = paymentService;
        this.paymentRepository = paymentRepository;
        this.notificationsRepository = notificationsRepository;
    }

    @Operation(summary = "Create TYPE1 payment")
    @PostMapping("createType1")
    public void createType1Payment(@Validated(FirstValidation.class) @RequestBody Payment payment) {
        Logger logger = Logger.getLogger(getClass().getName());
        try {
            paymentService.createType1Payment(payment);

            FileHandler handler = new FileHandler(LOG_NAME, true);
            SimpleFormatter formatter = new SimpleFormatter();
            logger.addHandler(handler);
            handler.setFormatter(formatter);
            logger.info(PAYMENT_NAME + payment.getPaymentType() + PAYMENT_CREATION);
            handler.close();
        } catch (Exception e) {
            logger.info("There was an error creating TYPE1 payment");
        }
    }

    @Operation(summary = "Create TYPE2 payment")
    @PostMapping("createType2")
    public void createType2Payment(@Validated(SecondValidation.class) @RequestBody Payment payment) {
        Logger logger = Logger.getLogger(getClass().getName());
        try {
            paymentService.createType2Payment(payment);

            FileHandler handler = new FileHandler(LOG_NAME, true);
            SimpleFormatter formatter = new SimpleFormatter();
            logger.addHandler(handler);
            handler.setFormatter(formatter);
            logger.info(PAYMENT_NAME + payment.getPaymentType() + PAYMENT_CREATION);
            handler.close();
        } catch (Exception e) {
            logger.info("There was an error creating TYPE2 payment");
        }
    }

    @Operation(summary = "Create TYPE3 payment")
    @PostMapping("createType3")
    public void createType3Payment(@Validated(ThirdValidation.class) @RequestBody Payment payment) {
        Logger logger = Logger.getLogger(getClass().getName());
        try {
            paymentService.createType3Payment(payment);

            FileHandler handler = new FileHandler(LOG_NAME, true);
            SimpleFormatter formatter = new SimpleFormatter();
            logger.addHandler(handler);
            handler.setFormatter(formatter);
            logger.info(PAYMENT_NAME + payment.getPaymentType() + PAYMENT_CREATION);
            handler.close();
        } catch (Exception e) {
            logger.info("There was an error creating TYPE3 payment");
        }
    }

    @Operation(summary = "Cancel specific payment")
    @PatchMapping("cancel")
    public void cancelSpecificPayment(@RequestParam int id) {
        Logger logger = Logger.getLogger(getClass().getName());
        try {
            paymentService.cancelPayment(id);

            FileHandler handler = new FileHandler(LOG_NAME, true);
            SimpleFormatter formatter = new SimpleFormatter();
            logger.addHandler(handler);
            handler.setFormatter(formatter);
            logger.info(PAYMENT_NAME + id + " has been canceled");
            handler.close();
        } catch (Exception e) {
            logger.info("There was an error cancelling payment");
        }
    }

    @Operation(summary = "Get valid payments")
    @GetMapping("valid")
    public List<Integer> getValidPayments(@RequestParam String paymentType) {
        Logger logger = Logger.getLogger(getClass().getName());
        try {
            FileHandler handler = new FileHandler(LOG_NAME, true);
            SimpleFormatter formatter = new SimpleFormatter();
            logger.addHandler(handler);
            handler.setFormatter(formatter);
            logger.info("All valid payments have been received");
            handler.close();
        } catch (Exception e) {
            logger.info("There was an error getting all valid payments");
        }
        return paymentService.getValidPayments(paymentType);
    }

    @Operation(summary = "Get specific payment")
    @GetMapping("specific")
    public CancellationDetails getSpecificPayment(@RequestParam int id) {
        Logger logger = Logger.getLogger(getClass().getName());
        try {
            FileHandler handler = new FileHandler(LOG_NAME, true);
            SimpleFormatter formatter = new SimpleFormatter();
            logger.addHandler(handler);
            handler.setFormatter(formatter);
            logger.info(PAYMENT_NAME + id + " has been received");
            handler.close();
        } catch (Exception e) {
            logger.info("There was an error getting specific payment");
        }
        return paymentService.getSpecificPayment(id);
    }

    @Operation(summary = "Get all payments")
    @GetMapping("data")
    public List<Payment> getAllPayments() {
        Logger logger = Logger.getLogger(getClass().getName());
        try {
            FileHandler handler = new FileHandler(LOG_NAME, true);
            SimpleFormatter formatter = new SimpleFormatter();
            logger.addHandler(handler);
            handler.setFormatter(formatter);
            logger.info("All payments have been received");
            handler.close();
        } catch (Exception e) {
            logger.info("There was an error getting all payments");
        }
        return paymentRepository.findAll();
    }

    @Operation(summary = "Get all notifications")
    @GetMapping("notificationDetails")
    public List<Notification> getNotificationDetails() {
        Logger logger = Logger.getLogger(getClass().getName());
        try {
            FileHandler handler = new FileHandler(LOG_NAME, true);
            SimpleFormatter formatter = new SimpleFormatter();
            logger.addHandler(handler);
            handler.setFormatter(formatter);
            logger.info("All notifications have been received");
            handler.close();
        } catch (Exception e) {
            logger.info("There was an error getting all notifications");
        }
        return notificationsRepository.findAll();
    }

    @Operation(summary = "Get user's IP address and country")
    @GetMapping("country")
    public void getCountry() {
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String ip = req.getRemoteAddr();
        File database = new File("GeoLite2-City.mmdb");
        Logger logger = Logger.getLogger(getClass().getName());

        try {
            DatabaseReader dbReader = new DatabaseReader.Builder(database).build();
            CityResponse response = dbReader.city(InetAddress.getByName(ip));
            FileHandler handler = new FileHandler(LOG_NAME, true);
            SimpleFormatter formatter = new SimpleFormatter();

            logger.addHandler(handler);
            handler.setFormatter(formatter);
            logger.info("IP: " + ip);
            logger.info("Country: " + response.getCountry().getName());
            handler.close();
        } catch (Exception e) {
            logger.info("No country has been found with " + ip + " IP address");
        }
    }
}
