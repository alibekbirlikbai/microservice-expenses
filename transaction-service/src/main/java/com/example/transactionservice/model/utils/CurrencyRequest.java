package com.example.transactionservice.model.utils;

import com.example.transactionservice.model.Currency;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class CurrencyRequest {
//    private long id;
    private String base;
    private String formatted_timestamp;
//    private List<Currency> currencyList;
}