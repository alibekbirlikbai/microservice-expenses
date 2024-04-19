package com.example.currencyservice.repository;

import com.example.currencyservice.model.Currency;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepo extends CrudRepository<Currency, Long> {
}
