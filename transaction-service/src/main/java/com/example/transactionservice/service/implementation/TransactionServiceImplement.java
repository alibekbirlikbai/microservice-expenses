package com.example.transactionservice.service.implementation;

import com.example.transactionservice.model.Transaction;
import com.example.transactionservice.repository.TransactionRepo;
import com.example.transactionservice.service.TransactionService;
import com.example.transactionservice.service.implementation.utils.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImplement implements TransactionService {
    TransactionRepo repository;

    @Autowired
    public TransactionServiceImplement(TransactionRepo repository) {
        this.repository = repository;
    }

    @Override
    public Transaction save(Transaction transaction) {
        ServiceUtils.validateAccountNumbersLength(transaction);
        return repository.save(transaction);
    }
}
