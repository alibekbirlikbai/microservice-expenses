package com.example.transactionservice.external.service;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public interface CurrencyService {
    BigDecimal convertToUSD(String currency_shortname, BigDecimal transaction_sum, ZonedDateTime transaction_dateTime);
}
