package com.banking.loanservice.service.impl;

import com.banking.loanservice.dto.LoanDto;
import com.banking.loanservice.entity.Loan;
import com.banking.loanservice.exception.LoanNotFoundException;
import com.banking.loanservice.repository.LoanRepository;
import com.banking.loanservice.service.LoanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Loan Service Implementation with EMI calculation
 * Author: Pratham Rathod | prathamrathod200@gmail.com
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;

    @Override
    public LoanDto applyForLoan(LoanDto dto) {
        log.info("Loan application received for customer: {}", dto.getCustomerId());

        BigDecimal emi = calculateEMI(dto.getPrincipalAmount(), dto.getInterestRate(), dto.getTenureMonths());
        BigDecimal totalRepayment = emi.multiply(BigDecimal.valueOf(dto.getTenureMonths()));
        BigDecimal totalInterest = totalRepayment.subtract(dto.getPrincipalAmount());
        BigDecimal processingFee = dto.getPrincipalAmount().multiply(new BigDecimal("0.01")).setScale(2, RoundingMode.HALF_UP);

        Loan loan = Loan.builder()
                .loanNumber(generateLoanNumber(dto.getLoanType()))
                .customerId(dto.getCustomerId())
                .accountNumber(dto.getAccountNumber())
                .loanType(dto.getLoanType())
                .principalAmount(dto.getPrincipalAmount())
                .outstandingAmount(dto.getPrincipalAmount())
                .interestRate(dto.getInterestRate())
                .tenureMonths(dto.getTenureMonths())
                .emiAmount(emi)
                .processingFee(processingFee)
                .totalInterest(totalInterest)
                .status(Loan.LoanStatus.APPLIED)
                .purpose(dto.getPurpose())
                .collateralDetails(dto.getCollateralDetails())
                .emisPaid(0)
                .emisRemaining(dto.getTenureMonths())
                .build();

        Loan saved = loanRepository.save(loan);
        log.info("Loan application submitted: {}", saved.getLoanNumber());
        return mapToDto(saved);
    }

    @Override
    public LoanDto approveLoan(String loanNumber) {
        Loan loan = findByLoanNumber(loanNumber);
        if (loan.getStatus() != Loan.LoanStatus.APPLIED && loan.getStatus() != Loan.LoanStatus.UNDER_REVIEW) {
            throw new IllegalStateException("Loan cannot be approved in current status: " + loan.getStatus());
        }
        loan.setStatus(Loan.LoanStatus.APPROVED);
        log.info("Loan approved: {}", loanNumber);
        return mapToDto(loanRepository.save(loan));
    }

    @Override
    public LoanDto rejectLoan(String loanNumber, String reason) {
        Loan loan = findByLoanNumber(loanNumber);
        loan.setStatus(Loan.LoanStatus.REJECTED);
        loan.setRejectionReason(reason);
        log.info("Loan rejected: {} - Reason: {}", loanNumber, reason);
        return mapToDto(loanRepository.save(loan));
    }

    @Override
    public LoanDto disburseLoan(String loanNumber) {
        Loan loan = findByLoanNumber(loanNumber);
        if (loan.getStatus() != Loan.LoanStatus.APPROVED) {
            throw new IllegalStateException("Only approved loans can be disbursed");
        }
        loan.setStatus(Loan.LoanStatus.DISBURSED);
        loan.setDisbursedAmount(loan.getPrincipalAmount());
        loan.setDisbursementDate(LocalDate.now());
        loan.setMaturityDate(LocalDate.now().plusMonths(loan.getTenureMonths()));
        loan.setNextEmiDate(LocalDate.now().plusMonths(1));
        log.info("Loan disbursed: {}", loanNumber);
        return mapToDto(loanRepository.save(loan));
    }

    @Override
    public LoanDto payEMI(String loanNumber, BigDecimal amount) {
        Loan loan = findByLoanNumber(loanNumber);
        if (loan.getStatus() != Loan.LoanStatus.DISBURSED && loan.getStatus() != Loan.LoanStatus.ACTIVE) {
            throw new IllegalStateException("EMI can only be paid for active loans");
        }
        loan.setStatus(Loan.LoanStatus.ACTIVE);
        loan.setOutstandingAmount(loan.getOutstandingAmount().subtract(amount).max(BigDecimal.ZERO));
        loan.setEmisPaid(loan.getEmisPaid() + 1);
        loan.setEmisRemaining(loan.getEmisRemaining() - 1);
        loan.setNextEmiDate(loan.getNextEmiDate().plusMonths(1));

        if (loan.getOutstandingAmount().compareTo(BigDecimal.ZERO) == 0 || loan.getEmisRemaining() <= 0) {
            loan.setStatus(Loan.LoanStatus.CLOSED);
            log.info("Loan fully repaid: {}", loanNumber);
        }
        return mapToDto(loanRepository.save(loan));
    }

    @Override
    @Transactional(readOnly = true)
    public LoanDto getLoanByLoanNumber(String loanNumber) {
        return mapToDto(findByLoanNumber(loanNumber));
    }

    @Override
    @Transactional(readOnly = true)
    public LoanDto getLoanById(Long id) {
        return mapToDto(loanRepository.findById(id)
                .orElseThrow(() -> new LoanNotFoundException("Loan not found with id: " + id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanDto> getLoansByCustomerId(String customerId) {
        return loanRepository.findByCustomerId(customerId).stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalOutstandingByCustomer(String customerId) {
        BigDecimal total = loanRepository.getTotalOutstandingByCustomer(customerId);
        return total != null ? total : BigDecimal.ZERO;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LoanDto> getAllLoans(Pageable pageable) {
        return loanRepository.findAll(pageable).map(this::mapToDto);
    }

    @Override
    public BigDecimal calculateEMI(BigDecimal principal, BigDecimal annualRate, int tenureMonths) {
        // EMI = P * r * (1+r)^n / ((1+r)^n - 1)
        BigDecimal monthlyRate = annualRate.divide(BigDecimal.valueOf(1200), 10, RoundingMode.HALF_UP);
        BigDecimal onePlusR = BigDecimal.ONE.add(monthlyRate);
        BigDecimal power = onePlusR.pow(tenureMonths, new MathContext(10));
        BigDecimal numerator = principal.multiply(monthlyRate).multiply(power);
        BigDecimal denominator = power.subtract(BigDecimal.ONE);
        return numerator.divide(denominator, 2, RoundingMode.HALF_UP);
    }

    private Loan findByLoanNumber(String loanNumber) {
        return loanRepository.findByLoanNumber(loanNumber)
                .orElseThrow(() -> new LoanNotFoundException("Loan not found: " + loanNumber));
    }

    private String generateLoanNumber(Loan.LoanType type) {
        String prefix = switch (type) {
            case HOME_LOAN -> "HL";
            case PERSONAL_LOAN -> "PL";
            case CAR_LOAN -> "CL";
            case EDUCATION_LOAN -> "EL";
            case BUSINESS_LOAN -> "BL";
            case GOLD_LOAN -> "GL";
            case AGRICULTURE_LOAN -> "AL";
        };
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String random = String.format("%06d", new Random().nextInt(999999));
        return prefix + timestamp + random;
    }

    private LoanDto mapToDto(Loan loan) {
        return LoanDto.builder()
                .id(loan.getId())
                .loanNumber(loan.getLoanNumber())
                .customerId(loan.getCustomerId())
                .accountNumber(loan.getAccountNumber())
                .loanType(loan.getLoanType())
                .principalAmount(loan.getPrincipalAmount())
                .outstandingAmount(loan.getOutstandingAmount())
                .interestRate(loan.getInterestRate())
                .tenureMonths(loan.getTenureMonths())
                .emiAmount(loan.getEmiAmount())
                .disbursedAmount(loan.getDisbursedAmount())
                .processingFee(loan.getProcessingFee())
                .totalInterest(loan.getTotalInterest())
                .status(loan.getStatus())
                .purpose(loan.getPurpose())
                .disbursementDate(loan.getDisbursementDate())
                .maturityDate(loan.getMaturityDate())
                .nextEmiDate(loan.getNextEmiDate())
                .emisPaid(loan.getEmisPaid())
                .emisRemaining(loan.getEmisRemaining())
                .rejectionReason(loan.getRejectionReason())
                .collateralDetails(loan.getCollateralDetails())
                .appliedAt(loan.getAppliedAt())
                .updatedAt(loan.getUpdatedAt())
                .build();
    }
}
