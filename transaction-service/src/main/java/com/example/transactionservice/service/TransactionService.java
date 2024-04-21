package com.example.transactionservice.service;

import com.example.transactionservice.model.Limit;
import com.example.transactionservice.model.Transaction;

import java.time.ZonedDateTime;
import java.util.List;

public interface TransactionService {
    Transaction save(Transaction transaction);
    List<Transaction> getClientTransactionListForMonth(ZonedDateTime transactionDateTime);
    boolean checkTransactionForExceed(Transaction transaction, Limit limit);
}
