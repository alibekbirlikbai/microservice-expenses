package com.example.currencyservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
//@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column
    private String currency_shortname;

    @Column
    private BigDecimal rate_to_USD;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "currency_api_response_id")
//    private CurrencyApiResponse currencyApiResponse;
}
