package com.example.transactionservice.service;

import com.example.transactionservice.model.Currency;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface CurrencyService {
    Map<String, BigDecimal> getListOfCurrency(List<Currency> currencyList);
    BigDecimal convertToUSD(BigDecimal amount, String currency);
    List<Currency> fetchCurrencies();
}
