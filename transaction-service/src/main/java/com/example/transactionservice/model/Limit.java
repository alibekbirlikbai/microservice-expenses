package com.example.transactionservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Component
@Entity @Table(name = "limits")
@Getter @Setter @NoArgsConstructor @ToString
public class Limit {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column
    private BigDecimal limit_sum;

    @Column
    private ZonedDateTime limit_datetime;

    @Column
    private String limit_currency_shortname;

    @Column
    @Enumerated(EnumType.STRING)
    private ExpenseCategory expense_category;
}
