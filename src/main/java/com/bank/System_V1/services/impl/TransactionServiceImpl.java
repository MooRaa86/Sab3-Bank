package com.bank.System_V1.services.impl;

import com.bank.System_V1.dto.TransactionDto;
import com.bank.System_V1.entity.Transaction;
import com.bank.System_V1.repository.transactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    transactionRepository transactionRepository;

    @Override
    public void saveTransaction(TransactionDto transactionDto) {
        Transaction transaction =  Transaction.builder()
                .accountNumber(transactionDto.getAccountNumber())
                .transactionType(transactionDto.getTransactionType())
                .amount(transactionDto.getAmount())
                .status("Success")
                .build();

        transactionRepository.save(transaction);
        System.out.println("Transaction Saved Successfully");
    }
}
