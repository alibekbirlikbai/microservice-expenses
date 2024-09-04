package com.example.transaction.service;

import com.example.transaction.model.entity.Limit;
import com.example.transaction.model.entity.Transaction;
import com.example.transaction.model.dto.ExceededTransactionDTO;

import java.time.ZonedDateTime;
import java.util.List;

public interface TransactionService {
    Transaction save(Transaction transaction);
    List<Transaction> getClientTransactionListForMonth(ZonedDateTime transactionDateTime);
    boolean checkTransactionForExceed(Transaction transaction, Limit limit);
    /** этот метод == getAllExceededTransactions_SQL()
     * но как Java код */
    List<ExceededTransactionDTO> getAllExceededTransactions_Java();
    /** этот метод == getAllExceededTransactions_Java()
     * но как SQL запрос */
//    List<ExceededTransactionDTO> getAllExceededTransactions_SQL();
}
