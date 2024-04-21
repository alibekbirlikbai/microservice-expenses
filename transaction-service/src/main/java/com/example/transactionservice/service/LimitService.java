package com.example.transactionservice.service;

import com.example.transactionservice.model.ExpenseCategory;
import com.example.transactionservice.model.Limit;
import com.example.transactionservice.model.Transaction;

import java.math.BigDecimal;

public interface LimitService {
    boolean hasRecords();
    Limit setDefaultLimit(Transaction transaction);
    BigDecimal calculateLimitSumLeft(Transaction transaction, Limit limit);
}
