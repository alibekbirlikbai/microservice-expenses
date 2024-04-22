package com.example.transactionservice.repository;

import com.example.transactionservice.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface TransactionRepo extends CrudRepository<Transaction, Long> {
    List<Transaction> findByDatetimeBetween(ZonedDateTime firstDayOfMonth, ZonedDateTime lastDayOfMonth);
}
