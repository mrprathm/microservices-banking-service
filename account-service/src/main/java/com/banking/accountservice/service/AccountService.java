package com.banking.accountservice.service;

import com.banking.accountservice.dto.AccountDto;
import com.banking.accountservice.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {

    AccountDto createAccount(AccountDto accountDto);

    AccountDto getAccountByAccountNumber(String accountNumber);

    AccountDto getAccountById(Long id);

    List<AccountDto> getAccountsByCustomerId(String customerId);

    AccountDto updateAccount(Long id, AccountDto accountDto);

    void closeAccount(String accountNumber);

    AccountDto deposit(String accountNumber, BigDecimal amount);

    AccountDto withdraw(String accountNumber, BigDecimal amount);

    BigDecimal getBalanceByAccountNumber(String accountNumber);

    BigDecimal getTotalBalanceByCustomerId(String customerId);

    AccountDto updateAccountStatus(String accountNumber, Account.AccountStatus status);

    Page<AccountDto> getAllAccounts(Pageable pageable);
}
