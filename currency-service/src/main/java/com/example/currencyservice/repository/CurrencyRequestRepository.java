package com.example.currencyservice.repository;

import com.example.currencyservice.model.dto.CurrencyRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CurrencyRequestRepository extends CrudRepository<CurrencyRequest, Long> {
    @Query("SELECT cr FROM CurrencyRequest cr WHERE cr.base = :base AND cr.formatted_timestamp = :formatted_timestamp")
    CurrencyRequest findByBaseAndFormatted_timestamp(
        @Param("base") String base,
        @Param("formatted_timestamp") String formatted_timestamp
    );

    @Query("SELECT cr FROM CurrencyRequest cr WHERE cr.formatted_timestamp = :formatted_timestamp")
    CurrencyRequest findByFormatted_timestamp(@Param("formatted_timestamp") String formatted_timestamp);
}
