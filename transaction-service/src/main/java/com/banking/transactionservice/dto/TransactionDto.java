package com.banking.transactionservice.dto;

import com.banking.transactionservice.entity.Transaction;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionDto {

    private Long id;
    private String transactionReference;

    @NotBlank(message = "Account number is required")
    private String accountNumber;

    private String beneficiaryAccount;
    private String beneficiaryName;
    private String beneficiaryIfsc;
    private Transaction.TransactionType transactionType;

    @NotNull(message = "Transaction mode is required")
    private Transaction.TransactionMode transactionMode;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "1.0", message = "Amount must be at least 1")
    private BigDecimal amount;

    private BigDecimal balanceBefore;
    private BigDecimal balanceAfter;
    private Transaction.TransactionStatus status;
    private String description;
    private String remarks;
    private String failureReason;
    private String utrNumber;
    private LocalDateTime transactionDate;
    private String channel;
}
