package com.example.transactionservice.service;

import com.example.transactionservice.model.ExpenseCategory;
import com.example.transactionservice.model.Limit;
import com.example.transactionservice.model.Transaction;

public interface LimitService {
    boolean hasRecords();
    Limit setDefaultLimits(ExpenseCategory expenseCategory);
}
