package com.example.transactionservice.service.implement;

import com.example.transactionservice.model.Limit;
import com.example.transactionservice.model.Transaction;
import com.example.transactionservice.repository.TransactionRepo;
import com.example.transactionservice.service.LimitService;
import com.example.transactionservice.service.TransactionService;
import com.example.transactionservice.service.implement.utils.ServiceUtils;
import com.example.transactionservice.service.implement.utils.TransactionServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImplement implements TransactionService {
    private TransactionRepo repository;
    private LimitService limitService;

    @Autowired
    public TransactionServiceImplement(TransactionRepo repository, LimitService limitService) {
        this.repository = repository;
        this.limitService = limitService;
    }


    @Override
    public Transaction save(Transaction transaction) {
        TransactionServiceUtils.validateAccountNumbersLength(transaction);
        ServiceUtils.roundToHundredth(transaction.getSum());

        Limit limit = null;

        if (!limitService.hasRecords()) {
            limit = limitService.setDefaultLimits(transaction.getExpense_category());

            System.out.println(limit.toString());
        } else {
            ///
        }

        return repository.save(transaction);
    }
}
