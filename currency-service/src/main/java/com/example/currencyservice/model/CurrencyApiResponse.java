package com.example.currencyservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
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
