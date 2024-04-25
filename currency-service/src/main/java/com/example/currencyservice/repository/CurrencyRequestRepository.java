package com.example.currencyservice.repository;

import com.example.currencyservice.model.dto.CurrencyRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CurrencyRequestRepository extends CrudRepository<CurrencyRequest, Long> {
    @Query("SELECT cr FROM CurrencyRequest cr WHERE cr.base = :base AND cr.formatted_timestamp = :formatted_timestamp")
    CurrencyRequest findByBaseAndFormatted_timestamp(String base, String formatted_timestamp);

    @Query("SELECT cr FROM CurrencyRequest cr WHERE cr.formatted_timestamp = :formatted_timestamp")
    CurrencyRequest findByFormatted_timestamp(String formatted_timestamp);
}
