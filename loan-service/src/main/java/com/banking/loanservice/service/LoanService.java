package com.banking.loanservice.service;

import com.banking.loanservice.dto.LoanDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface LoanService {
    LoanDto applyForLoan(LoanDto dto);
    LoanDto approveLoan(String loanNumber);
    LoanDto rejectLoan(String loanNumber, String reason);
    LoanDto disburseLoan(String loanNumber);
    LoanDto payEMI(String loanNumber, BigDecimal amount);
    LoanDto getLoanByLoanNumber(String loanNumber);
    LoanDto getLoanById(Long id);
    List<LoanDto> getLoansByCustomerId(String customerId);
    BigDecimal getTotalOutstandingByCustomer(String customerId);
    Page<LoanDto> getAllLoans(Pageable pageable);
    BigDecimal calculateEMI(BigDecimal principal, BigDecimal annualRate, int tenureMonths);
}
