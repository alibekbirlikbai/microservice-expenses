package com.example.transactionservice.service.implement;

import com.example.transactionservice.model.Limit;
import com.example.transactionservice.model.Transaction;
import com.example.transactionservice.repository.TransactionRepo;
import com.example.transactionservice.service.LimitService;
import com.example.transactionservice.service.TransactionService;
import com.example.transactionservice.service.implement.utils.ServiceUtils;
import com.example.transactionservice.service.implement.utils.TransactionServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.List;
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

        Limit limit = null;
        if (!limitService.hasRecords()) {
            // Лимит по умолчанию (1000.00)
            limit = limitService.setDefaultLimit(transaction);
//            System.out.println("limit.sum=" + limit.getLimit_sum() + " + limit.dateTime=" + limit.getLimit_datetime());
//            System.out.println("transaction.sum=" + transaction.getSum() + " + transaction.dateTime=" + transaction.getDatetime());
        } else {
            /// Лимит Клиента
        }

        transaction.setLimit_exceeded(checkTransactionForExceed(transaction, limit));

        return repository.save(transaction);
    }

    @Override
    public List<Transaction> getClientTransactionListForMonth(ZonedDateTime transactionDateTime) {
        // Определяем начальную и конечную даты текущего месяца
        ZonedDateTime firstDayOfMonth = ServiceUtils.getStartOfMonthDateTime(transactionDateTime);
        ZonedDateTime lastDayOfMonth = ServiceUtils.getEndOfMonthDateTime(transactionDateTime);

        // Получаем все транзакции клиента за месяц
        List<Transaction> transactions = repository.findByDatetimeBetween(
                firstDayOfMonth, lastDayOfMonth);
        return transactions;
    }

    @Override
    public boolean checkTransactionForExceed(Transaction transaction, Limit limit) {
        BigDecimal limitSumLeft = limitService.calculateLimitSumLeft(transaction, limit);
//        System.out.println(limitSumLeft);

        if (limitSumLeft.compareTo(BigDecimal.ZERO) < 0) {
            return true;
        }
        return false;
    }
}
