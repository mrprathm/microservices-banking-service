package com.banking.transactionservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Transaction Entity - Records all financial transactions
 * Author: Pratham Rathod | prathamrathod200@gmail.com
 */
@Entity
@Table(name = "transactions", indexes = {
    @Index(name = "idx_txn_reference", columnList = "transaction_reference", unique = true),
    @Index(name = "idx_txn_account", columnList = "account_number"),
    @Index(name = "idx_txn_date", columnList = "transaction_date")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_reference", unique = true, nullable = false, length = 30)
    private String transactionReference;

    @Column(name = "account_number", nullable = false, length = 20)
    private String accountNumber;

    @Column(name = "beneficiary_account", length = 20)
    private String beneficiaryAccount;

    @Column(name = "beneficiary_name", length = 100)
    private String beneficiaryName;

    @Column(name = "beneficiary_ifsc", length = 15)
    private String beneficiaryIfsc;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_mode", nullable = false)
    private TransactionMode transactionMode;

    @Column(name = "amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(name = "balance_before", precision = 15, scale = 2)
    private BigDecimal balanceBefore;

    @Column(name = "balance_after", precision = 15, scale = 2)
    private BigDecimal balanceAfter;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private TransactionStatus status = TransactionStatus.PENDING;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "remarks", length = 255)
    private String remarks;

    @Column(name = "failure_reason", length = 500)
    private String failureReason;

    @Column(name = "utr_number", length = 30)
    private String utrNumber;

    @CreationTimestamp
    @Column(name = "transaction_date", nullable = false, updatable = false)
    private LocalDateTime transactionDate;

    @Column(name = "value_date")
    private LocalDateTime valueDate;

    @Column(name = "channel", length = 50)
    @Builder.Default
    private String channel = "NET_BANKING";

    public enum TransactionType {
        CREDIT, DEBIT
    }

    public enum TransactionMode {
        NEFT, RTGS, IMPS, UPI, CASH, CHEQUE, ATM, ONLINE_TRANSFER, INTERNAL_TRANSFER
    }

    public enum TransactionStatus {
        PENDING, SUCCESS, FAILED, REVERSED, ON_HOLD
    }
}
