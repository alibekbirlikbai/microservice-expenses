package com.example.transactionservice.service.implement;

import com.example.transactionservice.model.ExpenseCategory;
import com.example.transactionservice.model.Limit;
import com.example.transactionservice.model.Transaction;
import com.example.transactionservice.repository.LimitRepo;
import com.example.transactionservice.service.CurrencyService;
import com.example.transactionservice.service.LimitService;
import com.example.transactionservice.service.TransactionService;
import com.example.transactionservice.service.implement.utils.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LimitServiceImplement implements LimitService {
    private LimitRepo repository;
    private TransactionService transactionService;
    private CurrencyService currencyService;

    @Autowired
    public LimitServiceImplement(LimitRepo repository,
                                 @Lazy TransactionService transactionService,
                                 @Lazy CurrencyService currencyService) {
        this.repository = repository;
        this.transactionService = transactionService;
        this.currencyService = currencyService;
    }


    @Override
    public boolean hasRecords() {
        return repository.findAll().iterator().hasNext();
    }

    @Override
    public Limit setDefaultLimit(Transaction transaction) {
        Limit limit = new Limit();
        limit.setId(0);
        limit.setLimit_currency_shortname("USD");
        limit.setLimit_sum(BigDecimal.valueOf(1000.00));
        limit.setLimit_datetime(ServiceUtils.getStartOfMonthDateTime(transaction.getDatetime()));
        limit.setExpense_category(transaction.getExpense_category());
        return limit;
    }

    @Override
    public Limit setClientLimit(Limit limit) {
        // Обновлять существующие лимиты запрещается
        checkLimitForExist(limit);

        /* автоматически выставляется текущая дата,
        * не позволяя выставить ее в прошедшем или будущем времени */
        limit.setLimit_datetime(ServiceUtils.getCurrentDateTime());
//        limit.setLimit_datetime(ServiceUtils.getTruncatedCurrentDateTime());

        // Лимит всегда USD
        limit.setLimit_currency_shortname("USD");
        return repository.save(limit);
    }

    @Override
    public BigDecimal calculateLimitSumLeft(Transaction transaction, Limit limit) {
        // (Это просто Лог) не используется в логике
        System.out.print("Transaction: " + transaction.getSum() + " " + transaction.getCurrency_shortname() + " -> ");
        BigDecimal transactionSumInUSD = currencyService.convertToUSD(transaction.getCurrency_shortname(), transaction.getSum(), transaction.getDatetime());
        System.out.println("in USD:" + transactionSumInUSD);


        ZonedDateTime limitStartDate = limit.getLimit_datetime();
        ZonedDateTime transactionDateTime = transaction.getDatetime();
        ExpenseCategory transactionCategory = transaction.getExpense_category();

        // Обновляем лимит на начало месяца транзакции, если она произошла после установления лимита
        if (transactionDateTime.isAfter(limitStartDate)) {
            limitStartDate = ZonedDateTime.of(transactionDateTime.getYear(), transactionDateTime.getMonthValue(), 1, 0, 0, 0, 0, transactionDateTime.getZone());
        }

        // Получаем все транзакции клиента за месяц
        List<Transaction> transactions = transactionService.getClientTransactionListForMonth(transactionDateTime);
        transactions.add(transaction);

        // Фильтруем транзакции, которые произошли после установления лимита в текущем месяце
        ZonedDateTime finalLimitStartDate = limitStartDate;
        List<Transaction> relevantTransactions = transactions.stream()
                .filter(t -> t.getDatetime().isAfter(finalLimitStartDate))
                .filter(t -> t.getExpense_category() == transactionCategory) // Учитываем категорию расходов
                .map(t -> {
                    Transaction transactionCopy = t.clone();

                    // Конвертируем сумму транзакции в USD
                    transactionCopy.setSum(currencyService.convertToUSD(t.getCurrency_shortname(), t.getSum(), t.getDatetime())); // Конвертируем сумму транзакции в USD
                    return transactionCopy;
                })
                .collect(Collectors.toList());

        // Вычисляем остаток лимита
        BigDecimal remainingLimit = limit.getLimit_sum();
        for (Transaction t : relevantTransactions) {
            remainingLimit = remainingLimit.subtract(t.getSum());
        }
        System.out.println("remainingLimit: " + remainingLimit);
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
    public Map<ExpenseCategory, Limit> getLatestLimitsForCategories() {
        Map<ExpenseCategory, Limit> latestLimits = new HashMap<>();

        for (ExpenseCategory category : ExpenseCategory.values()) {
            // Получаем список последних записей для каждой категории из базы данных
            List<Limit> limits = repository.findTopByExpenseCategoryOrderByLimitDatetimeDesc(category);

            // Если список не пуст и содержит элементы, добавляем первый элемент (самую последнюю запись) в карту
            if (!limits.isEmpty()) {
                latestLimits.put(category, limits.get(0));
            }
        }
        return latestLimits;
    }

    @Override
    public List<Limit> getAllLimits() {
        return (List<Limit>) repository.findAll();
    }

    @Override
    public Limit getLimitForTransaction(Transaction transaction) {
        // Получаем дату и время совершения транзакции
        ZonedDateTime transactionDateTime = transaction.getDatetime();

        // Получаем все лимиты для данной категории расходов, отсортированные по дате установления лимита в убывающем порядке
        List<Limit> limits = repository.findByExpense_categoryOrderByLimit_datetimeDesc(transaction.getExpense_category());

        // Находим первый лимит, установленный до даты и времени транзакции
        Limit limitForTransaction = limits.stream()
                .filter(limit -> limit.getLimit_datetime().isBefore(transactionDateTime))
                .findFirst()
                .orElse(null);

        if (limitForTransaction != null) {
            // Если лимит найден, возвращаем его
            return limitForTransaction;
        } else {
            // Если лимит не найден, возвращаем лимит по умолчанию
            return setDefaultLimit(transaction);
        }
    }

}
