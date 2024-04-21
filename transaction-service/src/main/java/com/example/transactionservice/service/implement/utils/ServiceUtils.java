package com.example.transactionservice.service.implement.utils;

import java.math.BigDecimal;
import java.time.*;

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

    public static ZonedDateTime getStartOfNextMonthDateTime(ZonedDateTime limitStartDate) {
        return limitStartDate.plusMonths(1).withDayOfMonth(1).with(LocalTime.MIN);
    }
}
