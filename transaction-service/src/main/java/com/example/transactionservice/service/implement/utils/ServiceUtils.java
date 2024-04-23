package com.example.transactionservice.service.implement.utils;

import com.example.transactionservice.model.ExpenseCategory;

import java.math.BigDecimal;
import java.time.*;
import java.util.Map;

public class ServiceUtils {
    public static BigDecimal roundToHundredth(BigDecimal value) {
        value.setScale(2, BigDecimal.ROUND_HALF_UP);
        return value;
    }

    public static ZonedDateTime getStartOfMonthDateTime(ZonedDateTime dateTime) {
        // Получаем первое число месяца с минимальным временем (00:00:00)
        ZonedDateTime firstDayOfMonth = dateTime.withDayOfMonth(1).with(LocalTime.MIN);
        return firstDayOfMonth;
    }

    public static ZonedDateTime getEndOfMonthDateTime(ZonedDateTime dateTime) {
        // Получаем последнее число месяца с максимальным временем (23:59:59)
        ZonedDateTime lastDayOfMonth = getStartOfMonthDateTime(dateTime).plusMonths(1).minusDays(1).with(LocalTime.MAX);
        return lastDayOfMonth;
    }

    public static ZonedDateTime getCurrentDateTime() {
        return ZonedDateTime.now();
    }

    public static ExpenseCategory parseExpenseCategory(Map<String, Object> map, String key) {
        String expenseCategoryString = (String) map.get(key);
        if (expenseCategoryString != null) {
            return ExpenseCategory.valueOf(expenseCategoryString.toUpperCase());
        }
        return null;
    }

    public static ZonedDateTime parseDateTime(Map<String, Object> map, String key) {
        Instant instant = (Instant) map.get(key);
        if (instant != null) {
            return ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());
        }
        return null;
    }
}
