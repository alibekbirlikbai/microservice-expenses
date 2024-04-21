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

//    public static boolean checkTransactionForExceed(Transaction transaction, Limit limit) {
//        // Проверяем, что транзакция произошла после установления лимита
//        if (transaction.getDatetime().isBefore(limit.getLimit_datetime())) {
//            return false; // Транзакция была совершена до установления лимита, флаг не выставляем
//        }
//
//        // Проверяем, что валюта транзакции совпадает с валютой лимита
//        if (!transaction.getCurrency_shortname().equals(limit.getLimit_currency_shortname())) {
//            return false; // Валюты не совпадают, флаг не выставляем
//        }
//
//        // Проверяем, что категории расходов совпадают
//        if (transaction.getExpense_category() != limit.getExpense_category()) {
//            return false; // Категории расходов не совпадают, флаг не выставляем
//        }
//
//        // Получаем сумму всех транзакций с учетом лимита
//        BigDecimal totalSum = calculateTotalSum(transaction, limit);
//
//        // Проверяем, превышает ли сумма всех транзакций лимит
//        if (totalSum.compareTo(limit.getLimit_sum()) > 0) {
//            return true; // Сумма превышает лимит, выставляем флаг
//        }
//
//        return false; // Сумма не превышает лимит, флаг не выставляем
//    }


//    private static BigDecimal calculateTotalSum(Transaction transaction, Limit limit) {
//        // Получаем сумму транзакции
//        BigDecimal transactionSum = transaction.getSum();
//
//        // Проверяем, что транзакция произошла после установления лимита
//        if (transaction.getDatetime().isBefore(limit.getLimit_datetime())) {
//            return transactionSum; // Если транзакция была совершена до установления лимита, возвращаем только ее сумму
//        }
//
//        // Получаем сумму всех транзакций после установления лимита
//        BigDecimal totalSum = limit.getLimit_sum();
//
//        // Если транзакция превышает лимит, добавляем ее сумму к общей сумме
//        if (transactionSum.compareTo(BigDecimal.ZERO) > 0) {
//            totalSum = totalSum.add(transactionSum);
//        }
//
//        return totalSum;
//    }
}
