package com.banking.notificationservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Notification {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "customer_id", nullable = false) private String customerId;
    @Column(name = "recipient_email") private String recipientEmail;
    @Column(name = "recipient_phone") private String recipientPhone;
    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type") private NotificationType notificationType;
    @Enumerated(EnumType.STRING)
    @Column(name = "channel") private NotificationChannel channel;
    @Column(name = "subject", length = 200) private String subject;
    @Column(name = "message", length = 2000) private String message;
    @Enumerated(EnumType.STRING)
    @Column(name = "status") @Builder.Default private NotificationStatus status = NotificationStatus.PENDING;
    @Column(name = "reference_id") private String referenceId;
    @Column(name = "failure_reason") private String failureReason;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false) private LocalDateTime createdAt;
    @Column(name = "sent_at") private LocalDateTime sentAt;

    public enum NotificationType { TRANSACTION_ALERT, LOAN_UPDATE, ACCOUNT_STATEMENT, OTP, WELCOME, KYC_UPDATE, LOGIN_ALERT }
    public enum NotificationChannel { EMAIL, SMS, PUSH }
    public enum NotificationStatus { PENDING, SENT, FAILED, DELIVERED }
}
