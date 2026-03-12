package com.banking.transactionservice.service.impl;

import com.banking.transactionservice.dto.TransactionDto;
import com.banking.transactionservice.entity.Transaction;
import com.banking.transactionservice.exception.TransactionNotFoundException;
import com.banking.transactionservice.repository.TransactionRepository;
import com.banking.transactionservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * Transaction Service Implementation
 * Author: Pratham Rathod | prathamrathod200@gmail.com
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Override
    public TransactionDto createTransaction(TransactionDto dto) {
        log.info("Processing transaction for account: {}", dto.getAccountNumber());

        Transaction transaction = Transaction.builder()
                .transactionReference(generateTransactionReference())
                .accountNumber(dto.getAccountNumber())
                .beneficiaryAccount(dto.getBeneficiaryAccount())
                .beneficiaryName(dto.getBeneficiaryName())
                .beneficiaryIfsc(dto.getBeneficiaryIfsc())
                .transactionType(dto.getTransactionType())
                .transactionMode(dto.getTransactionMode())
                .amount(dto.getAmount())
                .balanceBefore(dto.getBalanceBefore())
                .balanceAfter(dto.getBalanceAfter())
                .status(Transaction.TransactionStatus.SUCCESS)
                .description(dto.getDescription())
                .remarks(dto.getRemarks())
                .utrNumber(generateUTR())
                .valueDate(LocalDateTime.now())
                .channel(dto.getChannel() != null ? dto.getChannel() : "NET_BANKING")
                .build();

        Transaction saved = transactionRepository.save(transaction);
        log.info("Transaction created: {}", saved.getTransactionReference());
        return mapToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionDto getTransactionByReference(String reference) {
        Transaction txn = transactionRepository.findByTransactionReference(reference)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found: " + reference));
        return mapToDto(txn);
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionDto getTransactionById(Long id) {
        Transaction txn = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found with id: " + id));
        return mapToDto(txn);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionDto> getTransactionsByAccount(String accountNumber, Pageable pageable) {
        return transactionRepository.findByAccountNumber(accountNumber, pageable).map(this::mapToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionDto> getTransactionsByAccountAndType(String accountNumber, Transaction.TransactionType type, Pageable pageable) {
        return transactionRepository.findByAccountNumberAndTransactionType(accountNumber, type, pageable).map(this::mapToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionDto> getTransactionsByAccountAndDateRange(String accountNumber, LocalDateTime from, LocalDateTime to, Pageable pageable) {
        return transactionRepository.findByAccountNumberAndTransactionDateBetween(accountNumber, from, to, pageable).map(this::mapToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalCreditByAccount(String accountNumber) {
        BigDecimal total = transactionRepository.sumByAccountAndType(accountNumber, Transaction.TransactionType.CREDIT);
        return total != null ? total : BigDecimal.ZERO;
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalDebitByAccount(String accountNumber) {
        BigDecimal total = transactionRepository.sumByAccountAndType(accountNumber, Transaction.TransactionType.DEBIT);
        return total != null ? total : BigDecimal.ZERO;
    }

    @Override
    public TransactionDto reverseTransaction(String transactionReference) {
        Transaction txn = transactionRepository.findByTransactionReference(transactionReference)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found: " + transactionReference));

        if (txn.getStatus() != Transaction.TransactionStatus.SUCCESS) {
            throw new IllegalStateException("Only successful transactions can be reversed");
        }

        // Create reversal transaction
        Transaction reversal = Transaction.builder()
                .transactionReference(generateTransactionReference())
                .accountNumber(txn.getAccountNumber())
                .transactionType(txn.getTransactionType() == Transaction.TransactionType.DEBIT
                        ? Transaction.TransactionType.CREDIT : Transaction.TransactionType.DEBIT)
                .transactionMode(txn.getTransactionMode())
                .amount(txn.getAmount())
                .status(Transaction.TransactionStatus.SUCCESS)
                .description("REVERSAL of " + txn.getTransactionReference())
                .utrNumber(generateUTR())
                .valueDate(LocalDateTime.now())
                .channel("SYSTEM")
                .build();

        txn.setStatus(Transaction.TransactionStatus.REVERSED);
        transactionRepository.save(txn);
        Transaction savedReversal = transactionRepository.save(reversal);
        log.info("Transaction reversed: {} -> {}", transactionReference, savedReversal.getTransactionReference());
        return mapToDto(savedReversal);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionDto> getAllTransactions(Pageable pageable) {
        return transactionRepository.findAll(pageable).map(this::mapToDto);
    }

    private String generateTransactionReference() {
        String prefix = "TXN";
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = String.format("%04d", new Random().nextInt(9999));
        return prefix + timestamp + random;
    }

    private String generateUTR() {
        return "UTR" + System.currentTimeMillis() + String.format("%04d", new Random().nextInt(9999));
    }

    private TransactionDto mapToDto(Transaction txn) {
        return TransactionDto.builder()
                .id(txn.getId())
                .transactionReference(txn.getTransactionReference())
                .accountNumber(txn.getAccountNumber())
                .beneficiaryAccount(txn.getBeneficiaryAccount())
                .beneficiaryName(txn.getBeneficiaryName())
                .beneficiaryIfsc(txn.getBeneficiaryIfsc())
                .transactionType(txn.getTransactionType())
                .transactionMode(txn.getTransactionMode())
                .amount(txn.getAmount())
                .balanceBefore(txn.getBalanceBefore())
                .balanceAfter(txn.getBalanceAfter())
                .status(txn.getStatus())
                .description(txn.getDescription())
                .remarks(txn.getRemarks())
                .failureReason(txn.getFailureReason())
                .utrNumber(txn.getUtrNumber())
                .transactionDate(txn.getTransactionDate())
                .channel(txn.getChannel())
                .build();
    }
}
