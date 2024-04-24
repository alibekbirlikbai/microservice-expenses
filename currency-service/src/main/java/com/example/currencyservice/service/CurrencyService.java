package com.example.currencyservice.service;

import com.example.currencyservice.model.Currency;
import com.example.currencyservice.model.CurrencyApiResponse;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.util.List;

public interface CurrencyService {
    Mono<List<Currency>> getCurrencyList(ZonedDateTime dateTime);
    Mono<List<Currency>> createCurrenciesFromResponse(Mono<CurrencyApiResponse> responseMono);
//    Mono<String> test();
}
