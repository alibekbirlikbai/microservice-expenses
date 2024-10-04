package com.example.transaction.service.impl.utils;

import com.example.transaction.model.entity.Limit;

import java.math.BigDecimal;
import java.util.Map;

public class LimitServiceUtils {
    public static Limit convertMapToLimit(Map<String, Object> map) {
        Limit limit = new Limit();
        limit.setId((Long) map.get("id"));
        limit.setLimit_sum((BigDecimal) map.get("limit_sum"));
        limit.setLimit_datetime(ServiceUtils.parseDateTime(map, "limit_datetime"));
        limit.setLimit_currency_shortname((String) map.get("limit_currency_shortname"));
        limit.setExpense_category(ServiceUtils.parseExpenseCategory(map, "expense_category"));
        return limit;
    }
}
