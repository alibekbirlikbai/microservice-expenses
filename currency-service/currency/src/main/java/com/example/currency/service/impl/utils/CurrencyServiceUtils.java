package com.example.currency.service.impl.utils;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class CurrencyServiceUtils {
    public static ZonedDateTime getCurrentDateTime() {
        return ZonedDateTime.now();
    }

    /* Нужно конвертировать дату Транзакции
     * в формат даты который может принимать API  */
    public static String parseZoneDateTime(ZonedDateTime zonedDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = zonedDateTime.format(formatter);
        return formattedDate;
    }
}
