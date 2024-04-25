package com.example.transactionservice.model;

import com.example.transactionservice.model.utils.CurrencyRequest;
import lombok.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Component
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class Currency {
//    private long id;
    private String currency_shortname;
    private BigDecimal rate_to_USD;
    private CurrencyRequest currencyRequest;
}
