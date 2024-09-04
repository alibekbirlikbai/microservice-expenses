package com.example.transaction.model.dto;


import com.example.transaction.model.entity.Limit;
import com.example.transaction.model.entity.Transaction;
import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Getter @Setter @NoArgsConstructor @ToString
public class ExceededTransactionDTO {
    private Transaction transaction;
    private Limit limit;
}
