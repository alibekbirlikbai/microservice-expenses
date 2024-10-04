package com.example.currency.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Entity @Table(name = "currencies")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column
    private String currency_shortname;

    @Column
    private BigDecimal rate_to_USD;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "currency_request_id")
    @JsonManagedReference // Используется для инициирования ссылки
    /* это нужно чтобы при запросе мы не получали рекурсивную сериализацию,
     * т.е. когда параметр вызывает внутренний параметр,
     * внутри которого есть такой же параметр и так по кругу
     * (решает проблему - Infinite recursion (StackOverflowError))
     *      потратил несколько часов чтобы это понять */
    private CurrencyRequest currencyRequest;
}
