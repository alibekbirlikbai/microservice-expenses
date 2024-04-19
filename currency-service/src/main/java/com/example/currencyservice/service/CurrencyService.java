package com.example.currencyservice.service;

import com.example.currencyservice.model.Currency;
import com.example.currencyservice.repository.CurrencyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CurrencyService {
    private CurrencyRepo repository;

    @Autowired
    public CurrencyService(CurrencyRepo repository) {
        this.repository = repository;
    }

    public Currency save(Currency currency) {
        return repository.save(currency);
    }
}
