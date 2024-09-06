package com.example.currency.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.util.List;
import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Entity @Table(name = "currency_requests")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class CurrencyRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column
    private String base;

    @Column(unique = true)
    private String formatted_timestamp;

    @OneToMany(
            mappedBy = "currencyRequest",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    /** Используется для обратной ссылки, не будет сериализоваться
     * */
    @JsonBackReference
    private List<Currency> currencyList;
}
