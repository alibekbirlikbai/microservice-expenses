package com.example.currencyservice.repository;

import com.example.currencyservice.model.Currency;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurrencyRepository extends CrudRepository<Currency, Long> {
    @Query("SELECT c FROM Currency c WHERE c.currencyRequest.id = :currency_request_id")
    List<Currency> findAllByCurrencyRequestID(@Param("currency_request_id") long currency_request_id);
}
