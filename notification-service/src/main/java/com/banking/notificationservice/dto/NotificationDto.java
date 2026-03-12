package com.banking.notificationservice.dto;

import com.banking.notificationservice.entity.Notification;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationDto {
    private Long id;
    @NotBlank private String customerId;
    private String recipientEmail;
    private String recipientPhone;
    private Notification.NotificationType notificationType;
    private Notification.NotificationChannel channel;
    private String subject;
    @NotBlank private String message;
    private Notification.NotificationStatus status;
    private String referenceId;
    private String failureReason;
    private LocalDateTime createdAt;
    private LocalDateTime sentAt;
}
