package com.example.transaction.service.impl;

import com.example.transaction.model.entity.Limit;
import com.example.transaction.model.entity.Transaction;
import com.example.transaction.model.dto.ExceededTransactionDTO;
import com.example.transaction.model.enums.ExpenseCategory;
import com.example.transaction.repository.TransactionRepoJOOQ;
import com.example.transaction.repository.TransactionRepoJPA;
import com.example.transaction.service.LimitService;
import com.example.transaction.service.TransactionService;
import com.example.transaction.service.impl.utils.ServiceUtils;
import com.example.transaction.service.impl.utils.TransactionServiceUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.*;
import java.util.*;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepoJPA repositoryJPA;
    private final TransactionRepoJOOQ repositoryJOOQ;
    private final LimitService limitService;

    @Autowired
    public TransactionServiceImpl(
        TransactionRepoJPA repositoryJPA,
        TransactionRepoJOOQ repositoryJOOQ,
        @Lazy LimitService limitService
    ) {
        this.repositoryJPA = repositoryJPA;
        this.repositoryJOOQ = repositoryJOOQ;
        this.limitService = limitService;
    }


    @Override
    public Transaction save(Transaction transaction) {
        TransactionServiceUtils.validateTransactionData(transaction);

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
        return repositoryJPA.save(transaction);
    }

    @Override
    public List<Transaction> getClientTransactionListForMonth(ZonedDateTime dateTime) {
        // Определяем начальную и конечную даты текущего месяца
        ZonedDateTime firstDayOfMonth = ServiceUtils.getStartOfMonthDateTime(dateTime);
        ZonedDateTime lastDayOfMonth = ServiceUtils.getEndOfMonthDateTime(dateTime);

        // Получаем все транзакции клиента за месяц
        List<Transaction> transactions = repositoryJOOQ.findByDatetimeBetween(firstDayOfMonth, lastDayOfMonth);

        // Создаем копии транзакций, чтобы изменения применялись только к копиям
        List<Transaction> transactionCopyList = new ArrayList<>();
        for (Transaction transaction : transactions) {
            Transaction transactionCopy = transaction.clone();
            transactionCopyList.add(transactionCopy.clone());
        }
        return transactions;
    }

    @Override
    public boolean checkTransactionForExceed(Transaction transaction, Limit limit) {
        /* Чтобы не сохранять конвертированный (в USD) вариант как сумму для текущей транзакции,
         * при расчете limitSumLeft передаем в нее копию  */
        Transaction transactionCopy = transaction.clone();

        BigDecimal limitSumLeft = limitService.calculateLimitSumLeft(transactionCopy, limit);

        // Если limitSumLeft отрицательный или 0.0, то лимит был превышен
      return limitSumLeft.compareTo(BigDecimal.ZERO) < 0;
    }

    @Override
    public List<ExceededTransactionDTO> getAllExceededTransactions_Java() {
        // Получаем все транзакции, которые превысили лимиты
        List<Transaction> exceededTransactions = repositoryJOOQ.findByLimit_exceededTrue();

        // Создаем список DTO для превышенных транзакций
        List<ExceededTransactionDTO> exceededTransactionDTOs = new ArrayList<>();

        // Проходим по каждой превышенной транзакции
        for (Transaction transaction : exceededTransactions) {
            // Получаем лимит для данной транзакции
            Limit limit = limitService.getLimitForTransaction(transaction);

            // Создаем DTO и устанавливаем транзакцию и соответствующий лимит
            ExceededTransactionDTO dto = new ExceededTransactionDTO();
            dto.setTransaction(transaction);
            dto.setLimit(limit);

            // Добавляем DTO в список
            exceededTransactionDTOs.add(dto);
        }
        Collections.reverse(exceededTransactionDTOs);
        return exceededTransactionDTOs;
    }


//    @Override
//    public List<ExceededTransactionDTO> getAllExceededTransactions_SQL() {
//        return repositoryJOOQ.findExceededTransactionsWithLimits();
//    }
}
