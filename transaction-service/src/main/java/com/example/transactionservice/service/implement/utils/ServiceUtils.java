package com.example.transactionservice.service.implement.utils;

import java.math.BigDecimal;

public class ServiceUtils {
    public static BigDecimal roundToHundredth(BigDecimal value) {
        value.setScale(2, BigDecimal.ROUND_HALF_UP);
        return value;
    }
}
