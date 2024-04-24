package com.example.currencyservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
//@Entity
//@Table(name = "currency_api_response")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class CurrencyApiResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Transient
    private String disclaimer;

    @Transient
    private String license;

    @Column
    private String timestamp;

    @Column
    private String base;

//    @OneToMany(mappedBy = "currencyApiResponse", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<Currency> rates;
    private Map<String, BigDecimal> rates;
}
