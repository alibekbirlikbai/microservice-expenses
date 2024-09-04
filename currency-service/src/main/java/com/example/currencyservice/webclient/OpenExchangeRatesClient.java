package com.example.currencyservice.external;

import com.example.currencyservice.model.CurrencyApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class OpenExchangeRatesClient {
    private final WebClient webClient;

    @Value("${my.API_id}")
    private String app_id;

    @Value("${my.API_basePath}")
    private String basePath;

    @Autowired
    public OpenExchangeRatesClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .filter((request, next) -> {
                    System.out.println("Request: " + request.method() + " " + request.url());
                    return next.exchange(request);
                })
                .build();
    }

    public Mono<CurrencyApiResponse> getCurrencyList_Historical(String dateTime) {
        return webClient.get()
                .uri(uriBuilder ->
                        uriBuilder.scheme("https")
                                .host(basePath)
                                .path("/api/historical/" + dateTime + ".json")
                                .queryParam("app_id", app_id)
                                .build())
                .retrieve()
                .bodyToMono(CurrencyApiResponse.class);
    }

    public Mono<CurrencyApiResponse> getCurrencyList_Latest() {
        return webClient.get()
                .uri(uriBuilder ->
                        uriBuilder.scheme("https")
                                .host(basePath)
                                .path("/api/latest.json")
                                .queryParam("app_id", app_id)
                                .build())
                .retrieve()
                .bodyToMono(CurrencyApiResponse.class);
    }


//    public Mono<String> test() {
//        return webClient.get()
//                .uri("https://example.com")
//                .retrieve()
//                .bodyToMono(String.class);
//    }
}
