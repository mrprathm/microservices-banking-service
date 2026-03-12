package com.banking.transactionservice.service;

import com.banking.transactionservice.dto.TransactionDto;
import com.banking.transactionservice.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface TransactionService {
    TransactionDto createTransaction(TransactionDto dto);
    TransactionDto getTransactionByReference(String reference);
    TransactionDto getTransactionById(Long id);
    Page<TransactionDto> getTransactionsByAccount(String accountNumber, Pageable pageable);
    Page<TransactionDto> getTransactionsByAccountAndType(String accountNumber, Transaction.TransactionType type, Pageable pageable);
    Page<TransactionDto> getTransactionsByAccountAndDateRange(String accountNumber, LocalDateTime from, LocalDateTime to, Pageable pageable);
    BigDecimal getTotalCreditByAccount(String accountNumber);
    BigDecimal getTotalDebitByAccount(String accountNumber);
    TransactionDto reverseTransaction(String transactionReference);
    Page<TransactionDto> getAllTransactions(Pageable pageable);
}
