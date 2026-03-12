package com.banking.transactionservice.controller;

import com.banking.transactionservice.dto.ApiResponse;
import com.banking.transactionservice.dto.TransactionDto;
import com.banking.transactionservice.entity.Transaction;
import com.banking.transactionservice.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Transaction Controller
 * Author: Pratham Rathod | prathamrathod200@gmail.com | +91-9890394356
 */
@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Transaction Management", description = "APIs for managing bank transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    @Operation(summary = "Create/record a new transaction")
    public ResponseEntity<ApiResponse<TransactionDto>> createTransaction(@Valid @RequestBody TransactionDto dto) {
        TransactionDto created = transactionService.createTransaction(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Transaction recorded successfully", created));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get transaction by ID")
    public ResponseEntity<ApiResponse<TransactionDto>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Transaction fetched", transactionService.getTransactionById(id)));
    }

    @GetMapping("/reference/{reference}")
    @Operation(summary = "Get transaction by reference number")
    public ResponseEntity<ApiResponse<TransactionDto>> getByReference(@PathVariable String reference) {
        return ResponseEntity.ok(ApiResponse.success("Transaction fetched", transactionService.getTransactionByReference(reference)));
    }

    @GetMapping("/account/{accountNumber}")
    @Operation(summary = "Get all transactions for an account")
    public ResponseEntity<ApiResponse<Page<TransactionDto>>> getByAccount(
            @PathVariable String accountNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<TransactionDto> transactions = transactionService.getTransactionsByAccount(
                accountNumber, PageRequest.of(page, size, Sort.by("transactionDate").descending()));
        return ResponseEntity.ok(ApiResponse.success("Transactions fetched", transactions));
    }

    @GetMapping("/account/{accountNumber}/type/{type}")
    @Operation(summary = "Get transactions by account and type (CREDIT/DEBIT)")
    public ResponseEntity<ApiResponse<Page<TransactionDto>>> getByAccountAndType(
            @PathVariable String accountNumber,
            @PathVariable Transaction.TransactionType type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success("Transactions fetched",
                transactionService.getTransactionsByAccountAndType(accountNumber, type, PageRequest.of(page, size))));
    }

    @GetMapping("/account/{accountNumber}/date-range")
    @Operation(summary = "Get transactions by date range (mini statement)")
    public ResponseEntity<ApiResponse<Page<TransactionDto>>> getByDateRange(
            @PathVariable String accountNumber,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success("Transactions fetched",
                transactionService.getTransactionsByAccountAndDateRange(accountNumber, from, to, PageRequest.of(page, size))));
    }

    @GetMapping("/account/{accountNumber}/total-credit")
    @Operation(summary = "Get total credits for an account")
    public ResponseEntity<ApiResponse<BigDecimal>> getTotalCredit(@PathVariable String accountNumber) {
        return ResponseEntity.ok(ApiResponse.success("Total credit fetched", transactionService.getTotalCreditByAccount(accountNumber)));
    }

    @GetMapping("/account/{accountNumber}/total-debit")
    @Operation(summary = "Get total debits for an account")
    public ResponseEntity<ApiResponse<BigDecimal>> getTotalDebit(@PathVariable String accountNumber) {
        return ResponseEntity.ok(ApiResponse.success("Total debit fetched", transactionService.getTotalDebitByAccount(accountNumber)));
    }

    @PostMapping("/{transactionReference}/reverse")
    @Operation(summary = "Reverse a transaction")
    public ResponseEntity<ApiResponse<TransactionDto>> reverseTransaction(@PathVariable String transactionReference) {
        return ResponseEntity.ok(ApiResponse.success("Transaction reversed successfully", transactionService.reverseTransaction(transactionReference)));
    }

    @GetMapping
    @Operation(summary = "Get all transactions (admin)")
    public ResponseEntity<ApiResponse<Page<TransactionDto>>> getAllTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success("All transactions fetched",
                transactionService.getAllTransactions(PageRequest.of(page, size, Sort.by("transactionDate").descending()))));
    }
}
