package com.banking.loanservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Loan Entity - Bank Loan Product
 * Author: Pratham Rathod | prathamrathod200@gmail.com
 */
@Entity
@Table(name = "loans", indexes = {
    @Index(name = "idx_loan_number", columnList = "loan_number", unique = true),
    @Index(name = "idx_customer_loan", columnList = "customer_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "loan_number", unique = true, nullable = false, length = 25)
    private String loanNumber;

    @Column(name = "customer_id", nullable = false, length = 30)
    private String customerId;

    @Column(name = "account_number", length = 20)
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "loan_type", nullable = false)
    private LoanType loanType;

    @Column(name = "principal_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal principalAmount;

    @Column(name = "outstanding_amount", precision = 15, scale = 2)
    private BigDecimal outstandingAmount;

    @Column(name = "interest_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal interestRate;

    @Column(name = "tenure_months", nullable = false)
    private Integer tenureMonths;

    @Column(name = "emi_amount", precision = 15, scale = 2)
    private BigDecimal emiAmount;

    @Column(name = "disbursed_amount", precision = 15, scale = 2)
    private BigDecimal disbursedAmount;

    @Column(name = "processing_fee", precision = 10, scale = 2)
    private BigDecimal processingFee;

    @Column(name = "total_interest", precision = 15, scale = 2)
    private BigDecimal totalInterest;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private LoanStatus status = LoanStatus.APPLIED;

    @Column(name = "purpose", length = 500)
    private String purpose;

    @Column(name = "disbursement_date")
    private LocalDate disbursementDate;

    @Column(name = "maturity_date")
    private LocalDate maturityDate;

    @Column(name = "next_emi_date")
    private LocalDate nextEmiDate;

    @Column(name = "emis_paid")
    @Builder.Default
    private Integer emisPaid = 0;

    @Column(name = "emis_remaining")
    private Integer emisRemaining;

    @Column(name = "rejection_reason", length = 500)
    private String rejectionReason;

    @Column(name = "collateral_details", length = 1000)
    private String collateralDetails;

    @CreationTimestamp
    @Column(name = "applied_at", nullable = false, updatable = false)
    private LocalDateTime appliedAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum LoanType {
        HOME_LOAN, PERSONAL_LOAN, CAR_LOAN, EDUCATION_LOAN, BUSINESS_LOAN, GOLD_LOAN, AGRICULTURE_LOAN
    }

    public enum LoanStatus {
        APPLIED, UNDER_REVIEW, APPROVED, REJECTED, DISBURSED, ACTIVE, CLOSED, NPA, WRITTEN_OFF
    }
}
