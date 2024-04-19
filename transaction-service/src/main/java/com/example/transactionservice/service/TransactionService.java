package com.example.transactionservice.service;

import com.example.transactionservice.model.Transaction;

public interface TransactionService {
    Transaction save(Transaction transaction);
}
