package com.banking.accountservice.repository;

import com.banking.accountservice.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByAccountNumber(String accountNumber);

    List<Account> findByCustomerId(String customerId);

    List<Account> findByCustomerIdAndStatus(String customerId, Account.AccountStatus status);

    boolean existsByAccountNumber(String accountNumber);

    @Query("SELECT SUM(a.balance) FROM Account a WHERE a.customerId = :customerId AND a.status = 'ACTIVE'")
    BigDecimal getTotalBalanceByCustomerId(@Param("customerId") String customerId);

    Page<Account> findByStatus(Account.AccountStatus status, Pageable pageable);

    @Query("SELECT a FROM Account a WHERE a.balance < a.minimumBalance AND a.status = 'ACTIVE'")
    List<Account> findAccountsBelowMinimumBalance();
}
