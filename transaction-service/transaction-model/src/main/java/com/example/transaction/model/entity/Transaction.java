package com.example.transaction.model.entity;

import com.example.transaction.model.enums.ExpenseCategory;
import com.example.transaction.model.utils.LongToStringConverter;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import org.springframework.stereotype.Component;

@Component
@Entity @Table(name = "transactions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
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
