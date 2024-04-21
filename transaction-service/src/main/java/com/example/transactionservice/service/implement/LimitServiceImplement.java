package com.example.transactionservice.service.implement;

import com.example.transactionservice.model.ExpenseCategory;
import com.example.transactionservice.model.Limit;
import com.example.transactionservice.model.Transaction;
import com.example.transactionservice.repository.LimitRepo;
import com.example.transactionservice.service.LimitService;
import com.example.transactionservice.service.TransactionService;
import com.example.transactionservice.service.implement.utils.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LimitServiceImplement implements LimitService {
    private LimitRepo repository;
    private TransactionService transactionService;

    @Autowired
    public LimitServiceImplement(LimitRepo repository,
                                 @Lazy TransactionService transactionService) {
        this.repository = repository;
        this.transactionService = transactionService;
    }


    @Override
    public boolean hasRecords() {
        return repository.findAll().iterator().hasNext();
    }

    @Override
    public Limit setDefaultLimit(Transaction transaction) {
        Limit limit = new Limit();
        limit.setLimit_currency_shortname("USD");
        limit.setLimit_sum(BigDecimal.valueOf(1000.00));
        limit.setLimit_datetime(ServiceUtils.getStartOfMonthDateTime(transaction.getDatetime()));
        limit.setExpense_category(transaction.getExpense_category());
        return limit;
    }

    @Override
    public Limit setClientLimit(Limit limit) {
        // Обновлять существующие лимиты запрещается;
        checkLimitForExist(limit);

        limit.setLimit_datetime(ServiceUtils.getCurrentDateTime());
        return repository.save(limit);
    }

    @Override
    public BigDecimal calculateLimitSumLeft(Transaction transaction, Limit limit) {
        ZonedDateTime limitStartDate = limit.getLimit_datetime();
        ZonedDateTime nextMonthStart = ServiceUtils.getStartOfNextMonthDateTime(limitStartDate);
        ZonedDateTime transactionDateTime = transaction.getDatetime();
        ExpenseCategory transactionCategory = transaction.getExpense_category();

        // Получаем месяц у переданной транзакции
        int transactionMonth = transactionDateTime.getMonthValue();
        int limitMonth = limitStartDate.getMonthValue();

        /* Если месяц у переданной транзакции отличается от месяца установки лимита,
         обновляем лимит на начало нового месяца */
        if (transactionMonth != limitMonth) {
            limitStartDate = ZonedDateTime.of(transactionDateTime.getYear(), transactionMonth, 1, 0, 0, 0, 0, transactionDateTime.getZone());
            nextMonthStart = ServiceUtils.getStartOfNextMonthDateTime(limitStartDate);
        }

        // Получаем все транзакции клиента за месяц
        List<Transaction> transactions = transactionService.getClientTransactionListForMonth(transactionDateTime);
        transactions.add(transaction);

        /* Фильтруем транзакции, которые произошли после установления лимита
        но до начала следующего месяца, и соответствуют той же категории расходов */
        ZonedDateTime finalLimitStartDate = limitStartDate;
        ZonedDateTime finalNextMonthStart = nextMonthStart;
        List<Transaction> relevantTransactions = transactions.stream()
                .filter(t -> t.getDatetime().isAfter(finalLimitStartDate))
                .filter(t -> !t.getDatetime().isAfter(finalNextMonthStart))
                .filter(t -> t.getExpense_category() == transactionCategory) // Учитываем категорию расходов
                .collect(Collectors.toList());

        // Вычисляем остаток лимита
        BigDecimal remainingLimit = limit.getLimit_sum();
        for (Transaction t : relevantTransactions) {
            remainingLimit = remainingLimit.subtract(t.getSum());
        }

        return remainingLimit;
    }

    @Override
    public boolean checkLimitForExist(Limit limit) {
        if (limit.getId() > 0 && repository.existsById(limit.getId())) {
            throw new IllegalArgumentException("Limit already exists!!!");
        }
        return false;
    }

    @Override
    public Map<ExpenseCategory, Limit> latestLimitsForCategories() {
        Map<ExpenseCategory, Limit> latestLimits = new HashMap<>();

        for (ExpenseCategory category : ExpenseCategory.values()) {
            // Получаем последнюю запись для каждой ExpenseCategory из бд
            Limit latestLimit = repository.findTopByExpenseCategoryOrderByLimitDatetimeDesc(category);

            if (latestLimit != null) {
                latestLimits.put(category, latestLimit);
            }
        }

        return latestLimits;
    }

}
