package com.example.transactionservice.service;

import com.example.transactionservice.model.ExpenseCategory;
import com.example.transactionservice.model.Limit;
import com.example.transactionservice.model.Transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface LimitService {
    boolean hasRecords();
    Limit setDefaultLimit(Transaction transaction);
    Limit setClientLimit(Limit limit);
    boolean checkLimitForExist(Limit limit);
    BigDecimal calculateLimitSumLeft(Transaction transaction, Limit limit);
    Map<ExpenseCategory, Limit> latestLimitsForCategories();
}
