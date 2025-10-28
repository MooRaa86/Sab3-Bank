package com.bank.System_V1.repository;

import com.bank.System_V1.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.time.LocalDateTime;

@Repository
public interface transactionRepository extends JpaRepository<Transaction, String> {
    List<Transaction> findByAccountNumberAndDateBetween(
            String accountNumber,
            LocalDate startDate,
            LocalDate endDate
    );
}
