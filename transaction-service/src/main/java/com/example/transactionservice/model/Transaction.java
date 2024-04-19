package com.example.transactionservice.model;

import com.example.transactionservice.model.utils.LongToStringConverter;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Entity
@Getter @Setter @NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Convert(converter = LongToStringConverter.class)
    @Column(columnDefinition = "VARCHAR(10)")
    private long account_from;

    @Convert(converter = LongToStringConverter.class)
    @Column(columnDefinition = "VARCHAR(10)")
    private long account_to;
}
