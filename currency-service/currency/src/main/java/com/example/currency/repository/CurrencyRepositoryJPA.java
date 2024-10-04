package com.example.currency.repository;

import com.example.currency.model.entity.Currency;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepositoryJPA extends CrudRepository<Currency, Long> {}
