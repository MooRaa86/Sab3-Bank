package com.bank.System_V1.services.impl;

import com.bank.System_V1.dto.TransactionDto;
import com.bank.System_V1.entity.Transaction;

public interface TransactionService {
    void saveTransaction(TransactionDto transactionDto);
}
