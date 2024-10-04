package com.example.transaction.model.utils;

import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class CurrencyRequest {
//    private long id;
    private String base;
    private String formatted_timestamp;
//    private List<Currency> currencyList;
}
