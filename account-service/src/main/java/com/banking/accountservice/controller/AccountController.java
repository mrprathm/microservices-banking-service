package com.banking.accountservice.controller;

import com.banking.accountservice.dto.AccountDto;
import com.banking.accountservice.dto.ApiResponse;
import com.banking.accountservice.entity.Account;
import com.banking.accountservice.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Account Controller - REST APIs for Account Management
 * Author: Pratham Rathod | prathamrathod200@gmail.com
 */
@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Account Management", description = "APIs for managing bank accounts")
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    @Operation(summary = "Open new bank account")
    public ResponseEntity<ApiResponse<AccountDto>> createAccount(@Valid @RequestBody AccountDto accountDto) {
        AccountDto created = accountService.createAccount(accountDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Account opened successfully", created));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get account by ID")
    public ResponseEntity<ApiResponse<AccountDto>> getAccountById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Account fetched", accountService.getAccountById(id)));
    }

    @GetMapping("/account-number/{accountNumber}")
    @Operation(summary = "Get account by account number")
    public ResponseEntity<ApiResponse<AccountDto>> getAccountByNumber(@PathVariable String accountNumber) {
        return ResponseEntity.ok(ApiResponse.success("Account fetched", accountService.getAccountByAccountNumber(accountNumber)));
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get all accounts for a customer")
    public ResponseEntity<ApiResponse<List<AccountDto>>> getAccountsByCustomer(@PathVariable String customerId) {
        return ResponseEntity.ok(ApiResponse.success("Accounts fetched", accountService.getAccountsByCustomerId(customerId)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update account details")
    public ResponseEntity<ApiResponse<AccountDto>> updateAccount(@PathVariable Long id, @Valid @RequestBody AccountDto accountDto) {
        return ResponseEntity.ok(ApiResponse.success("Account updated", accountService.updateAccount(id, accountDto)));
    }

    @DeleteMapping("/account-number/{accountNumber}/close")
    @Operation(summary = "Close bank account")
    public ResponseEntity<ApiResponse<Void>> closeAccount(@PathVariable String accountNumber) {
        accountService.closeAccount(accountNumber);
        return ResponseEntity.ok(ApiResponse.success("Account closed successfully", null));
    }

    @PostMapping("/{accountNumber}/deposit")
    @Operation(summary = "Deposit money into account")
    public ResponseEntity<ApiResponse<AccountDto>> deposit(
            @PathVariable String accountNumber,
            @RequestParam BigDecimal amount) {
        return ResponseEntity.ok(ApiResponse.success("Amount deposited successfully", accountService.deposit(accountNumber, amount)));
    }

    @PostMapping("/{accountNumber}/withdraw")
    @Operation(summary = "Withdraw money from account")
    public ResponseEntity<ApiResponse<AccountDto>> withdraw(
            @PathVariable String accountNumber,
            @RequestParam BigDecimal amount) {
        return ResponseEntity.ok(ApiResponse.success("Amount withdrawn successfully", accountService.withdraw(accountNumber, amount)));
    }

    @GetMapping("/{accountNumber}/balance")
    @Operation(summary = "Get current balance")
    public ResponseEntity<ApiResponse<BigDecimal>> getBalance(@PathVariable String accountNumber) {
        return ResponseEntity.ok(ApiResponse.success("Balance fetched", accountService.getBalanceByAccountNumber(accountNumber)));
    }

    @GetMapping("/customer/{customerId}/total-balance")
    @Operation(summary = "Get total balance across all accounts")
    public ResponseEntity<ApiResponse<BigDecimal>> getTotalBalance(@PathVariable String customerId) {
        return ResponseEntity.ok(ApiResponse.success("Total balance fetched", accountService.getTotalBalanceByCustomerId(customerId)));
    }

    @PatchMapping("/{accountNumber}/status")
    @Operation(summary = "Update account status (freeze/unfreeze)")
    public ResponseEntity<ApiResponse<AccountDto>> updateStatus(
            @PathVariable String accountNumber,
            @RequestParam Account.AccountStatus status) {
        return ResponseEntity.ok(ApiResponse.success("Account status updated", accountService.updateAccountStatus(accountNumber, status)));
    }

    @GetMapping
    @Operation(summary = "Get all accounts with pagination")
    public ResponseEntity<ApiResponse<Page<AccountDto>>> getAllAccounts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success("Accounts fetched",
                accountService.getAllAccounts(PageRequest.of(page, size, Sort.by("createdAt").descending()))));
    }
}
