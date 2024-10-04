package com.example.transaction.webclient;

import com.example.transaction.model.entity.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.util.List;

@Component
public class CurrencyServiceClient {
    private final WebClient webClient;

    @Autowired
    public CurrencyServiceClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .filter((request, next) -> {
                    System.out.println("Request: " + request.method() + " " + request.url());
                    return next.exchange(request);
                })
                .build();
    }

    // http://localhost:8082/api/currency-management/{dateTime}
    public Mono<List<Currency>> getCurrencyList(ZonedDateTime dateTime) {
        return webClient.get()
                .uri(uriBuilder ->
                        uriBuilder.scheme("http")
                                .host("localhost")
                                .port(8082)
                                .path("/currency-service/api/currencies/" + dateTime)
                                .build())
                .retrieve()
                .bodyToFlux(Currency.class)  // Преобразуем ответ в поток объектов Currency
                .collectList();              // Собираем объекты Currency в список
    }
}
