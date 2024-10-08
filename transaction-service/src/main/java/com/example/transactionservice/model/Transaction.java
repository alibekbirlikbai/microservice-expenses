package com.example.transactionservice.model;

import com.example.transactionservice.model.utils.LongToStringConverter;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Component
@Entity
@Getter @Setter @NoArgsConstructor @ToString
public class Transaction implements Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Convert(converter = LongToStringConverter.class)
    @Column(columnDefinition = "VARCHAR(10)")
    private long account_from;

    @Convert(converter = LongToStringConverter.class)
    @Column(columnDefinition = "VARCHAR(10)")
    private long account_to;

    @Column
    private String currency_shortname;

    @Column
    private BigDecimal sum;

    @Column
    @Enumerated(EnumType.STRING)
    private ExpenseCategory expense_category;

    @Column
    private ZonedDateTime datetime;

    @Column
    private boolean limit_exceeded;

    @Override
    public Transaction clone() {
        try {
            Transaction clonedTransaction = (Transaction) super.clone();
            // Глубокое клонирование BigDecimal
            clonedTransaction.setSum(new BigDecimal(this.sum.toString()));
            return clonedTransaction;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
