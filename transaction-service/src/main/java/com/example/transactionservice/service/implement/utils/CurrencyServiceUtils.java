package com.example.transactionservice.external.service.implement.utils;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class CurrencyServiceUtils {
    public static String validateDateTimeToAPI(ZonedDateTime transaction_dateTime) {
        /* https://openexchangerates.org/api/
         * принимает дату в формате ISO-8601
         * поэтому конвертируем дату транзакции в этот формат */
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = transaction_dateTime.format(formatter);
        return formattedDate;
    }
}
