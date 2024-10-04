package com.example.currency.model.dto;

import lombok.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@Component
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class CurrencyApiResponse {
    private String disclaimer;
    private String license;
    private String timestamp;
    private String base;
    private Map<String, BigDecimal> rates;
}
