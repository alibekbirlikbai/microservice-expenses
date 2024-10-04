package com.example.currency.repository;

import com.example.currency.model.entity.CurrencyRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRequestRepositoryJPA extends CrudRepository<CurrencyRequest, Long> {}
