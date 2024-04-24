package com.example.currencyservice.model.dto;

import com.example.currencyservice.model.Currency;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class CurrencyRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column
    private String base;

    @Column
    private String formatted_timestamp;

    @OneToMany(
            mappedBy = "currencyRequest",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    @JsonBackReference // Используется для обратной ссылки, не будет сериализоваться
    private List<Currency> currencyList;
}