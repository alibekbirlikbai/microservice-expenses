package com.example.transactionservice.repository;

import com.example.transactionservice.model.Limit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LimitRepo extends CrudRepository<Limit, Long> {
}
