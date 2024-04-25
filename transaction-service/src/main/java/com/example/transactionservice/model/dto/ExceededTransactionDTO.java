package com.example.transactionservice.model.dto;

import com.example.transactionservice.model.Limit;
import com.example.transactionservice.model.Transaction;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Component
@Getter @Setter @NoArgsConstructor @ToString
public class ExceededTransactionDTO {
    private Transaction transaction;
    private Limit limit;
}
