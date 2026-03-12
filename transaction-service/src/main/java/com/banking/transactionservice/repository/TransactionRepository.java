package com.banking.transactionservice.repository;

import com.banking.transactionservice.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByTransactionReference(String ref);
    Page<Transaction> findByAccountNumber(String accountNumber, Pageable pageable);
    Page<Transaction> findByAccountNumberAndTransactionType(String accountNumber, Transaction.TransactionType type, Pageable pageable);
    Page<Transaction> findByAccountNumberAndTransactionDateBetween(String accountNumber, LocalDateTime from, LocalDateTime to, Pageable pageable);
    
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.accountNumber = :accountNumber AND t.transactionType = :type AND t.status = 'SUCCESS'")
    BigDecimal sumByAccountAndType(@Param("accountNumber") String accountNumber, @Param("type") Transaction.TransactionType type);
    
    long countByAccountNumberAndStatus(String accountNumber, Transaction.TransactionStatus status);
}
