package com.example.transaction.service;

import com.example.transaction.model.entity.Limit;
import com.example.transaction.model.entity.Transaction;

import com.example.transaction.model.enums.ExpenseCategory;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface LimitService {
    boolean hasRecords();
    Limit setDefaultLimit(Transaction transaction);
    Limit setClientLimit(Limit limit);
    boolean checkLimitForExist(Limit limit);
    BigDecimal calculateLimitSumLeft(Transaction transaction, Limit limit);
    Map<ExpenseCategory, Limit> getLatestLimitsForCategories();
    List<Limit> getAllLimits();
    Limit getLimitForTransaction(Transaction transaction);
}
