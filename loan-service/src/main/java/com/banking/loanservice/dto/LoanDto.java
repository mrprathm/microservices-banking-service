package com.banking.loanservice.dto;

import com.banking.loanservice.entity.Loan;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoanDto {
    private Long id;
    private String loanNumber;
    @NotBlank(message = "Customer ID is required")
    private String customerId;
    private String accountNumber;
    @NotNull(message = "Loan type is required")
    private Loan.LoanType loanType;
    @NotNull @DecimalMin("10000")
    private BigDecimal principalAmount;
    private BigDecimal outstandingAmount;
    @NotNull @DecimalMin("1.0") @DecimalMax("30.0")
    private BigDecimal interestRate;
    @NotNull @Min(1) @Max(360)
    private Integer tenureMonths;
    private BigDecimal emiAmount;
    private BigDecimal disbursedAmount;
    private BigDecimal processingFee;
    private BigDecimal totalInterest;
    private Loan.LoanStatus status;
    private String purpose;
    private LocalDate disbursementDate;
    private LocalDate maturityDate;
    private LocalDate nextEmiDate;
    private Integer emisPaid;
    private Integer emisRemaining;
    private String rejectionReason;
    private String collateralDetails;
    private LocalDateTime appliedAt;
    private LocalDateTime updatedAt;
}
