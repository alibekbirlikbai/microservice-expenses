package com.example.transactionservice.service.implement.utils;

import com.example.transactionservice.model.Limit;
import com.example.transactionservice.model.Transaction;
import java.math.BigDecimal;
import java.util.Map;

public class TransactionServiceUtils {
    public static void validateTransactionData(Transaction transaction) {
        validateAccountNumbersLength(transaction);
        ServiceUtils.roundToHundredth(transaction.getSum());

        // Это если для проверки логики сохранения
        // transaction.setDatetime(ServiceUtils.getCurrentDateTime());
    }

    private static void validateAccountNumbersLength(Transaction transaction) {
        if (String.valueOf(transaction.getAccount_from()).length()  > 10 ||
                String.valueOf(transaction.getAccount_to()).length()  > 10) {
            throw new IllegalArgumentException("Account number must be exactly 10 digits!!! (Not saved)");
        }
    }

    public static Transaction convertMapToTransaction(Map<String, Object> map) {
        Transaction transaction = new Transaction();
        transaction.setId((Long) map.get("id"));
        transaction.setAccount_from(Long.parseLong(String.valueOf(map.get("account_from"))));
        transaction.setAccount_to(Long.parseLong(String.valueOf(map.get("account_to"))));
        transaction.setCurrency_shortname((String) map.get("currency_shortname"));
        transaction.setSum((BigDecimal) map.get("sum"));
        transaction.setExpense_category(ServiceUtils.parseExpenseCategory(map, "expense_category"));
        transaction.setDatetime(ServiceUtils.parseDateTime(map, "datetime"));
        transaction.setLimit_exceeded((Boolean) map.get("limit_exceeded"));
        return transaction;
    }
}
