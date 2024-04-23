package com.example.transactionservice.model;

import lombok.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class Currency {
    private String currency_shortname;
    private BigDecimal rate_to_USD;
}
