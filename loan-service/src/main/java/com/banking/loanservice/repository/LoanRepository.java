package com.banking.loanservice.repository;

import com.banking.loanservice.entity.Loan;
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
public interface LoanRepository extends JpaRepository<Loan, Long> {
    Optional<Loan> findByLoanNumber(String loanNumber);
    List<Loan> findByCustomerId(String customerId);
    List<Loan> findByCustomerIdAndStatus(String customerId, Loan.LoanStatus status);
    Page<Loan> findByStatus(Loan.LoanStatus status, Pageable pageable);
    @Query("SELECT SUM(l.outstandingAmount) FROM Loan l WHERE l.customerId = :customerId AND l.status = 'ACTIVE'")
    BigDecimal getTotalOutstandingByCustomer(@Param("customerId") String customerId);
    long countByCustomerIdAndStatus(String customerId, Loan.LoanStatus status);
}
