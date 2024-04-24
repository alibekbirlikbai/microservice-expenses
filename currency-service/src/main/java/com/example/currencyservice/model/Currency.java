package com.example.currencyservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column
    private String currency_shortname;

    @Column
    private BigDecimal rate_to_USD;

    // добавить отношение между таблицами?
    @Column
    private long dateTime_id;
//    private String rate_dateTime;
}
