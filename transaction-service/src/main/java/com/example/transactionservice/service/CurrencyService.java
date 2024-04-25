package com.example.transactionservice.service;

import com.example.transactionservice.model.Currency;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

public interface CurrencyService {
    BigDecimal convertToUSD(String currency_shortname, BigDecimal transaction_sum, ZonedDateTime transaction_dateTime);
    Map<String, BigDecimal> getListOfCurrency(List<Currency> currencyList);
    Mono<List<Currency>> fetchCurrencyList(ZonedDateTime transaction_dateTime);
}
