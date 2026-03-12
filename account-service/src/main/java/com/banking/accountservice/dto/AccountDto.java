package com.banking.accountservice.dto;

import com.banking.accountservice.entity.Account;
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
public class AccountDto {

    private Long id;
    private String accountNumber;

    @NotBlank(message = "Customer ID is required")
    private String customerId;

    @NotNull(message = "Account type is required")
    private Account.AccountType accountType;

    private BigDecimal balance;

    @DecimalMin(value = "0.0", message = "Minimum balance cannot be negative")
    private BigDecimal minimumBalance;

    private Account.AccountStatus status;
    private String ifscCode;
    private String branchName;
    private String branchCode;
    private BigDecimal interestRate;
    private String nomineeName;
    private String nomineeRelation;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
