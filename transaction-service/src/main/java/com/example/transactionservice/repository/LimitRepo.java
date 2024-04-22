package com.example.transactionservice.repository;

import com.example.transactionservice.model.ExpenseCategory;
import com.example.transactionservice.model.Limit;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface LimitRepo extends CrudRepository<Limit, Long> {
    // Запрос для получения самой последней записи о лимите для ExpenseCategory
    @Query("SELECT l FROM Limit l WHERE l.expense_category = :category ORDER BY l.limit_datetime DESC")
    List<Limit> findTopByExpenseCategoryOrderByLimitDatetimeDesc(ExpenseCategory category);

    //    @Query("SELECT l FROM Limit l WHERE l.expense_category = :expenseCategory")
    //    Limit findByExpense_category(ExpenseCategory expenseCategory);

    @Query("SELECT l FROM Limit l WHERE l.limit_datetime BETWEEN :firstDayOfMonth AND :lastDayOfMonth")
    List<Limit> findByLimit_datetimeBetween(@Param("firstDayOfMonth") ZonedDateTime firstDayOfMonth,
                                            @Param("lastDayOfMonth") ZonedDateTime lastDayOfMonth);
}
