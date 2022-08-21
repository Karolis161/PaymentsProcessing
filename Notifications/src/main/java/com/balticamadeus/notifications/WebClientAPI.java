package com.balticamadeus.notifications;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
public class WebClientAPI {

    private final WebClient webClient = WebClient.create("http://localhost:8080/api/admin/payment");
//    For Docker uncomment line below and comment line above
//    private final WebClient webClient = WebClient.create("http://payments:8080/api/admin/payment");

    @PostMapping("/notification")
    public HttpStatus getResult(@RequestBody Integer id) {
        var specPayment = webClient.get()
                .uri("/specific" + "?id=" + id)
                .headers(headers -> headers.setBasicAuth("namas", "namas"))
                .retrieve()
                .bodyToMono(CancellationDetails.class)
                .block();


        if (specPayment != null) {
            return HttpStatus.OK;
        } else {
            return HttpStatus.BAD_REQUEST;
        }
    }
}
