package com.example.currencyservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
//@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class CurrencyApiResponse {
    private long id;

    private String disclaimer;

    private String license;

    private long timestamp;

    private String base;

    /*  Решение проблемы - Basic attribute type should not be a map
        (когда буду сохранять ответ запроса в бд)
    @ElementCollection
    @CollectionTable(name = "currency_rates")
    @MapKeyColumn(name = "currency_code")
    @Column(name = "rate") */
    private Map<String, Double> rates;
}
