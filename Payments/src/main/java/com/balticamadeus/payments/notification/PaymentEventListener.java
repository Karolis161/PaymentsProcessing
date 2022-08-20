package com.balticamadeus.payments.notification;

import com.balticamadeus.payments.entities.Notification;
import com.balticamadeus.payments.repository.NotificationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class PaymentEventListener {

    private final WebClient webClient = WebClient.create("http://localhost:8081");
    private final NotificationsRepository notificationsRepository;

    @Autowired
    public PaymentEventListener(NotificationsRepository notificationsRepository) {
        this.notificationsRepository = notificationsRepository;
    }

    @EventListener
    public void onValidPaymentEvent(PaymentEvent event) {
        var responseSpec = webClient.post()
                .uri("/notification")
                .body(Mono.just(event.getPaymentId()), Integer.class)
                .headers(headers -> headers.setBasicAuth("namas", "namas"))
                .retrieve()
                .toBodilessEntity()
                .block();

        Notification notification = new Notification();

        if (responseSpec.getStatusCode() == HttpStatus.OK) {
            notification.setPaymentId(event.getPaymentId());
            notification.setStatus(responseSpec.getStatusCode().toString());
            notificationsRepository.save(notification);
        } else {
            notification.setPaymentId(event.getPaymentId());
            notification.setStatus(responseSpec.getStatusCode().toString());
            notificationsRepository.save(notification);
        }
    }
}
