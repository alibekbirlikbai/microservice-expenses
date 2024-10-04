package com.example.transaction.repository;

import com.example.transaction.model.entity.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepoJPA extends CrudRepository<Transaction, Long> {}
