package com.example.transaction.repository;

import com.example.transaction.model.entity.Limit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LimitRepoJPA extends CrudRepository<Limit, Long> {}
