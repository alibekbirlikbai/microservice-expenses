package com.example.transaction.model.entity;

import com.example.transaction.model.utils.CurrencyRequest;
import lombok.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class Currency {
//    private long id;
    private String currency_shortname;
    private BigDecimal rate_to_USD;
    private CurrencyRequest currencyRequest;
}
