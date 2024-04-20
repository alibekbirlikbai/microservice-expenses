package com.example.transactionservice.service.implement.utils;

import com.example.transactionservice.model.Limit;
import com.example.transactionservice.model.Transaction;
import java.math.BigDecimal;

public class TransactionServiceUtils {
    public static void validateAccountNumbersLength(Transaction transaction) {
        if (String.valueOf(transaction.getAccount_from()).length()  > 10 ||
                String.valueOf(transaction.getAccount_to()).length()  > 10) {
            throw new IllegalArgumentException("Account number must be exactly 10 digits!!! (Not saved)");
        }
    }

    public static boolean checkTransactionForExceed(Transaction transaction, Limit limit) {


        return false;
    }
}
