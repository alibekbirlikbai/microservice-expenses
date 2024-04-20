package com.example.transactionservice.service.implement;

import com.example.transactionservice.model.ExpenseCategory;
import com.example.transactionservice.model.Limit;
import com.example.transactionservice.repository.LimitRepo;
import com.example.transactionservice.service.LimitService;
import com.example.transactionservice.service.implement.utils.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LimitServiceImplement implements LimitService {
    private LimitRepo repository;

    @Autowired
    public LimitServiceImplement(LimitRepo repository) {
        this.repository = repository;
    }


    @Override
    public boolean hasRecords() {
        return repository.findAll().iterator().hasNext();
    }

    @Override
    public Limit setDefaultLimits(ExpenseCategory expenseCategory) {
        Limit limit = new Limit();
        limit.setLimit_currency_shortname("USD");
        limit.setLimit_sum(BigDecimal.valueOf(1000.00));
        limit.setExpense_category(expenseCategory);

        return limit;
    }

}
