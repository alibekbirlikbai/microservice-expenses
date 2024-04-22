package com.example.transactionservice.service;

import com.example.transactionservice.model.Limit;
import com.example.transactionservice.model.Transaction;
import com.example.transactionservice.model.dto.ExceededTransactionDTO;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

public interface TransactionService {
    Transaction save(Transaction transaction);
    List<Transaction> getClientTransactionListForMonth(ZonedDateTime transactionDateTime);
    boolean checkTransactionForExceed(Transaction transaction, Limit limit);
    List<ExceededTransactionDTO> getAllExceededTransactions();
}
