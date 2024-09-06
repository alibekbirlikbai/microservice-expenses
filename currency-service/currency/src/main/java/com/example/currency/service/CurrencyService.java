package com.example.currency.service;

import com.example.currency.model.entity.CurrencyRequest;
import com.example.currency.model.entity.Currency;
import com.example.currency.model.dto.CurrencyApiResponse;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.util.List;

public interface CurrencyService {
    Mono<List<Currency>> getCurrencyList(ZonedDateTime dateTime);
    Mono<List<Currency>> createCurrenciesFromResponse(Mono<CurrencyApiResponse> responseMono);
    CurrencyRequest getCurrencyRequest(CurrencyApiResponse response);
    Mono<List<Currency>> checkForUnavailableRate(ZonedDateTime transaction_dateTime, List<Currency> currencyList);

    List<CurrencyRequest> getPastRequestList();
    List<Currency> getPastCurrencyList();
}
