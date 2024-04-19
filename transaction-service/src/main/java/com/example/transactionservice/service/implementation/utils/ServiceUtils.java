package com.example.transactionservice.service.implementation.utils;

import com.example.transactionservice.model.Transaction;

import java.math.BigDecimal;

public class ServiceUtils {
    public static void validateAccountNumbersLength(Transaction transaction) {
        if (String.valueOf(transaction.getAccount_from()).length()  > 10 ||
                String.valueOf(transaction.getAccount_to()).length()  > 10) {
            throw new IllegalArgumentException("Account number must be exactly 10 digits!!! (Not saved)");
        }
    }

    public static BigDecimal roundTransactionSum(BigDecimal value) {
        return value.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
