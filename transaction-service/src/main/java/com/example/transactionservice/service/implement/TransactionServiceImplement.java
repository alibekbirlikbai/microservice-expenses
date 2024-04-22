package com.example.transactionservice.service.implement;

import com.example.transactionservice.model.ExpenseCategory;
import com.example.transactionservice.model.Limit;
import com.example.transactionservice.model.Transaction;
import com.example.transactionservice.repository.TransactionRepo;
import com.example.transactionservice.service.LimitService;
import com.example.transactionservice.service.TransactionService;
import com.example.transactionservice.service.implement.utils.ServiceUtils;
import com.example.transactionservice.service.implement.utils.TransactionServiceUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImplement implements TransactionService {
    private TransactionRepo repository;
    private LimitService limitService;

    @Autowired
    public TransactionServiceImplement(TransactionRepo repository,
                                       @Lazy LimitService limitService) {
        this.repository = repository;
        this.limitService = limitService;
    }


    @Override
    public Transaction save(Transaction transaction) {
        TransactionServiceUtils.validateAccountNumbersLength(transaction);
        ServiceUtils.roundToHundredth(transaction.getSum());

        Limit limit = new Limit();
        if (limitService.hasRecords()) {
            Map<ExpenseCategory, Limit> latestLimits = limitService.getLatestLimitsForCategories();
            // Проверяем, соответствует ли ExpenseCategory транзакции одной из категорий в возвращаемой Map
            Limit limitForTransactionCategory = latestLimits.get(transaction.getExpense_category());

            if (limitForTransactionCategory != null) {
                /// Лимит Клиента
                BeanUtils.copyProperties(limitForTransactionCategory, limit);
                ServiceUtils.roundToHundredth(limit.getLimit_sum());
            } else {
                // Лимит по умолчанию (1000.00)
                /* на тот случай если Клиент установил лимит
                 * только для 1 из категорий ExpenseCategory */
                limit = limitService.setDefaultLimit(transaction);
            }
        } else {
            // Лимит по умолчанию (1000.00)
            /* на тот случай если Клиент никогда не устанавливал своего лимита
             * т.е. для всех ExpenseCategory в месяце в котором была совершена транзакция
             * лимит = 1000.00 */
            limit = limitService.setDefaultLimit(transaction);
        }
        transaction.setLimit_exceeded(checkTransactionForExceed(transaction, limit));
        return repository.save(transaction);
    }

    @Override
    public List<Transaction> getClientTransactionListForMonth(ZonedDateTime dateTime) {
        // Определяем начальную и конечную даты текущего месяца
        ZonedDateTime firstDayOfMonth = ServiceUtils.getStartOfMonthDateTime(dateTime);
        ZonedDateTime lastDayOfMonth = ServiceUtils.getEndOfMonthDateTime(dateTime);

        // Получаем все транзакции клиента за месяц
        List<Transaction> transactions = repository.findByDatetimeBetween(
                firstDayOfMonth, lastDayOfMonth);
        return transactions;
    }

    @Override
    public boolean checkTransactionForExceed(Transaction transaction, Limit limit) {
        BigDecimal limitSumLeft = limitService.calculateLimitSumLeft(transaction, limit);

        // Если limitSumLeft отрицательный или 0.0, то лимит был превышен
        if (limitSumLeft.compareTo(BigDecimal.ZERO) < 0) {
            return true;
        }
        return false;
    }
}
