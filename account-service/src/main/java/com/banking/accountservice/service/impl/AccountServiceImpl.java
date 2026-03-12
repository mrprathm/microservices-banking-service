package com.banking.accountservice.service.impl;

import com.banking.accountservice.dto.AccountDto;
import com.banking.accountservice.entity.Account;
import com.banking.accountservice.exception.AccountNotFoundException;
import com.banking.accountservice.exception.InsufficientBalanceException;
import com.banking.accountservice.repository.AccountRepository;
import com.banking.accountservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Account Service Implementation
 * Author: Pratham Rathod
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public AccountDto createAccount(AccountDto dto) {
        log.info("Creating account for customer: {}", dto.getCustomerId());

        Account account = Account.builder()
                .accountNumber(generateAccountNumber())
                .customerId(dto.getCustomerId())
                .accountType(dto.getAccountType())
                .balance(dto.getBalance() != null ? dto.getBalance() : BigDecimal.ZERO)
                .minimumBalance(getDefaultMinBalance(dto.getAccountType()))
                .status(Account.AccountStatus.ACTIVE)
                .ifscCode("BNKG0001234")
                .branchName("Pune Main Branch")
                .branchCode("PNQ001")
                .interestRate(getInterestRate(dto.getAccountType()))
                .nomineeName(dto.getNomineeName())
                .nomineeRelation(dto.getNomineeRelation())
                .build();

        Account saved = accountRepository.save(account);
        log.info("Account created: {}", saved.getAccountNumber());
        return mapToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public AccountDto getAccountByAccountNumber(String accountNumber) {
        return mapToDto(findAccountByNumber(accountNumber));
    }

    @Override
    @Transactional(readOnly = true)
    public AccountDto getAccountById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + id));
        return mapToDto(account);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountDto> getAccountsByCustomerId(String customerId) {
        return accountRepository.findByCustomerId(customerId)
                .stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public AccountDto updateAccount(Long id, AccountDto dto) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + id));
        account.setNomineeName(dto.getNomineeName());
        account.setNomineeRelation(dto.getNomineeRelation());
        return mapToDto(accountRepository.save(account));
    }

    @Override
    public void closeAccount(String accountNumber) {
        Account account = findAccountByNumber(accountNumber);
        account.setStatus(Account.AccountStatus.CLOSED);
        account.setClosedAt(LocalDateTime.now());
        accountRepository.save(account);
        log.info("Account closed: {}", accountNumber);
    }

    @Override
    public AccountDto deposit(String accountNumber, BigDecimal amount) {
        log.info("Depositing {} to account {}", amount, accountNumber);
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        Account account = findAccountByNumber(accountNumber);
        if (account.getStatus() != Account.AccountStatus.ACTIVE) {
            throw new IllegalStateException("Account is not active: " + accountNumber);
        }
        account.setBalance(account.getBalance().add(amount));
        return mapToDto(accountRepository.save(account));
    }

    @Override
    public AccountDto withdraw(String accountNumber, BigDecimal amount) {
        log.info("Withdrawing {} from account {}", amount, accountNumber);
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        Account account = findAccountByNumber(accountNumber);
        if (account.getStatus() != Account.AccountStatus.ACTIVE) {
            throw new IllegalStateException("Account is not active");
        }
        BigDecimal newBalance = account.getBalance().subtract(amount);
        if (newBalance.compareTo(account.getMinimumBalance() != null ? account.getMinimumBalance() : BigDecimal.ZERO) < 0) {
            throw new InsufficientBalanceException("Insufficient balance. Available: " + account.getBalance());
        }
        account.setBalance(newBalance);
        return mapToDto(accountRepository.save(account));
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getBalanceByAccountNumber(String accountNumber) {
        return findAccountByNumber(accountNumber).getBalance();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalBalanceByCustomerId(String customerId) {
        BigDecimal total = accountRepository.getTotalBalanceByCustomerId(customerId);
        return total != null ? total : BigDecimal.ZERO;
    }

    @Override
    public AccountDto updateAccountStatus(String accountNumber, Account.AccountStatus status) {
        Account account = findAccountByNumber(accountNumber);
        account.setStatus(status);
        return mapToDto(accountRepository.save(account));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccountDto> getAllAccounts(Pageable pageable) {
        return accountRepository.findAll(pageable).map(this::mapToDto);
    }

    private Account findAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountNumber));
    }

    private String generateAccountNumber() {
        String prefix = "1000";
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        String random = String.format("%06d", new Random().nextInt(999999));
        return prefix + timestamp + random;
    }

    private BigDecimal getDefaultMinBalance(Account.AccountType type) {
        return switch (type) {
            case SAVINGS -> new BigDecimal("1000");
            case CURRENT -> new BigDecimal("5000");
            case SALARY -> BigDecimal.ZERO;
            default -> new BigDecimal("500");
        };
    }

    private BigDecimal getInterestRate(Account.AccountType type) {
        return switch (type) {
            case SAVINGS -> new BigDecimal("3.5");
            case FIXED_DEPOSIT -> new BigDecimal("6.5");
            case RECURRING_DEPOSIT -> new BigDecimal("6.0");
            default -> BigDecimal.ZERO;
        };
    }

    private AccountDto mapToDto(Account account) {
        return AccountDto.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .customerId(account.getCustomerId())
                .accountType(account.getAccountType())
                .balance(account.getBalance())
                .minimumBalance(account.getMinimumBalance())
                .status(account.getStatus())
                .ifscCode(account.getIfscCode())
                .branchName(account.getBranchName())
                .branchCode(account.getBranchCode())
                .interestRate(account.getInterestRate())
                .nomineeName(account.getNomineeName())
                .nomineeRelation(account.getNomineeRelation())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .build();
    }
}
