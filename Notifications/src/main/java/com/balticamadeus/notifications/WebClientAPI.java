package com.balticamadeus.notifications;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
public class WebClientAPI {

    private WebClient webClient = WebClient.create("http://localhost:8080/api/admin/payment");

    @PostMapping("/notification")
    public HttpStatus getResult(@RequestBody Integer id) {

        var responseSp = webClient.get()
                .uri("/specific" + "?id=" + id)
                .headers(headers -> headers.setBasicAuth("namas", "namas"))
                .retrieve()
                .bodyToMono(CancellationDetails.class)
                .block();

        return HttpStatus.OK;
    }
}
