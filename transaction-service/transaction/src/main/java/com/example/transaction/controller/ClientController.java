package com.example.transaction.controller;

import com.example.transaction.model.entity.Limit;
import com.example.transaction.model.dto.ExceededTransactionDTO;
import com.example.transaction.service.LimitService;
import com.example.transaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transaction-service/api/client")
public class ClientController {
    private final LimitService limitService;
    private final TransactionService transactionService;

    @Autowired
    public ClientController(LimitService limitService, TransactionService transactionService) {
        this.limitService = limitService;
        this.transactionService = transactionService;
    }

    @PostMapping("/limits")
    public ResponseEntity<?> setClientLimit(@RequestBody Limit limit) {
        try {
            limitService.setClientLimit(limit);
            return ResponseEntity.status(HttpStatus.CREATED).body("New limit Successfully saved");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/limits")
    public List<Limit> getClientLimits() {
        return limitService.getAllLimits();
    }

    @GetMapping("/transactions/exceeded/java-code")
    public List<ExceededTransactionDTO> getTransactionsExceeded_Java() {
        return transactionService.getAllExceededTransactions_Java();
    }

//    @GetMapping("/transactions/exceeded/sql-query")
//    public List<ExceededTransactionDTO> getTransactionsExceeded_SQL() {
//        return transactionService.getAllExceededTransactions_SQL();
//    }
}
