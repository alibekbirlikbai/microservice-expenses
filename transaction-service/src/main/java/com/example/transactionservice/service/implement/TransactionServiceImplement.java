package com.example.transactionservice.service.implement;

import com.example.transactionservice.model.ExpenseCategory;
import com.example.transactionservice.model.Limit;
import com.example.transactionservice.model.Transaction;
import com.example.transactionservice.model.dto.ExceededTransactionDTO;
import com.example.transactionservice.repository.TransactionRepo;
import com.example.transactionservice.service.LimitService;
import com.example.transactionservice.service.TransactionService;
import com.example.transactionservice.service.implement.utils.LimitServiceUtils;
import com.example.transactionservice.service.implement.utils.ServiceUtils;
import com.example.transactionservice.service.implement.utils.TransactionServiceUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class TransactionServiceImplement implements TransactionService {
    private TransactionRepo repository;
    private LimitService limitService;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public TransactionServiceImplement(TransactionRepo repository,
                                       @Lazy LimitService limitService) {
        this.repository = repository;
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
        if (limitSumLeft.compareTo(BigDecimal.ZERO) < 0) {
            return true;
        }
        return false;
    }

    /* этот метод == getAllExceededTransactions_SQL()
     * но как Java код */
    @Override
    public List<ExceededTransactionDTO> getAllExceededTransactions_Java() {
        // Получаем все транзакции, которые превысили лимиты
        List<Transaction> exceededTransactions = repository.findByLimit_exceededTrue();

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

    /* этот метод == getAllExceededTransactions_Java()
     * но как SQL запрос */
    @Override
    public List<ExceededTransactionDTO> getAllExceededTransactions_SQL() {
        String sqlQuery = "SELECT t.id AS t_id, " +
                "    t.account_from, " +
                "    t.account_to, " +
                "    t.currency_shortname, " +
                "    t.sum, " +
                "    t.expense_category, " +
                "    t.datetime, " +
                "    t.limit_exceeded, " +
                "   CASE " +
                "        WHEN COALESCE(l.id, -1) = -1 THEN 0 " +
                "        ELSE l.id " +
                "    END AS limit_id, " +
                "   CASE " +
                "        WHEN COALESCE(l.id, 0) = 0 THEN 1000.00 " +
                "        ELSE l.limit_sum " +
                "    END AS limit_sum, " +
                "    CASE " +
                "        WHEN COALESCE(l.id, 0) = 0 THEN DATE_TRUNC('month', t.datetime) + INTERVAL '1 DAY' " +
                "        ELSE l.limit_datetime " +
                "    END AS limit_datetime, " +
                "    CASE " +
                "        WHEN COALESCE(l.id, 0) = 0 THEN 'USD' " +
                "        ELSE l.limit_currency_shortname " +
                "    END AS limit_currency_shortname, " +
                "    CASE " +
                "        WHEN COALESCE(l.id, 0) = 0 THEN t.expense_category " +
                "        ELSE COALESCE(l.expense_category, t.expense_category) " +
                "    END AS limit_expense_category " +
                "FROM Transaction t " +
                "LEFT JOIN ( " +
                "    SELECT t.id, MAX(l.limit_datetime) AS max_limit_datetime " +
                "    FROM Transaction t " +
                "    JOIN Limits l ON t.expense_category = l.expense_category AND t.datetime >= l.limit_datetime " + // Фильтрация транзакции по (категории) + (времени)
                "    WHERE t.limit_exceeded = true " +
                "    GROUP BY t.id " +
                ") AS t_max_limit ON t.id = t_max_limit.id AND t.limit_exceeded = true " +
                "LEFT JOIN Limits l ON t.expense_category = l.expense_category " +
                "            AND l.limit_datetime = t_max_limit.max_limit_datetime " +
                "WHERE t.limit_exceeded = true " + // Добавляем фильтрацию по limit_exceeded
                "ORDER BY t.datetime DESC"; // Сортировка ответа, чтобы сначала были самые актуальные транзакции

        Query query = entityManager.createNativeQuery(sqlQuery);
        List<Object[]> resultList = query.getResultList();

        List<ExceededTransactionDTO> exceededTransactionDTOs = new ArrayList<>();
        for (Object[] result : resultList) {
            // Проверяем значение limit_exceeded
            if ((Boolean) result[7]) {
                Map<String, Object> transactionMap = new LinkedHashMap<>();
                transactionMap.put("id", result[0]);
                transactionMap.put("account_from", result[1]);
                transactionMap.put("account_to", result[2]);
                transactionMap.put("currency_shortname", result[3]);
                transactionMap.put("sum", result[4]);
                transactionMap.put("expense_category", result[5]);
                transactionMap.put("datetime", result[6]);
                transactionMap.put("limit_exceeded", result[7]);

                ExceededTransactionDTO dto = new ExceededTransactionDTO();
                Map<String, Object> limitMap = new LinkedHashMap<>();

                // Check if limit_id is not equal to 0 before setting the limit
                if ((Long) result[8] != 0) {
                    // Если есть Client limits
                    limitMap.put("id", result[8]);
                    limitMap.put("limit_sum", result[9]);
                    limitMap.put("limit_datetime", result[10]);
                    limitMap.put("limit_currency_shortname", result[11]);
                    limitMap.put("expense_category", result[12]);

                    dto.setLimit(LimitServiceUtils.convertMapToLimit(limitMap));
                } else {
                    // Если нет Client limits
                    Limit defaultLimit = limitService.setDefaultLimit(TransactionServiceUtils.convertMapToTransaction(transactionMap));

                    limitMap.put("id", defaultLimit.getId());
                    limitMap.put("limit_sum", defaultLimit.getLimit_sum());
                    limitMap.put("limit_datetime", defaultLimit.getLimit_datetime());
                    limitMap.put("limit_currency_shortname", defaultLimit.getLimit_currency_shortname());
                    limitMap.put("expense_category", defaultLimit.getExpense_category());
                    dto.setLimit(defaultLimit);
                }

                dto.setTransaction(TransactionServiceUtils.convertMapToTransaction(transactionMap));

                exceededTransactionDTOs.add(dto);
            }
        }

        return exceededTransactionDTOs;
    }
}
