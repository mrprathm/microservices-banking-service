package com.banking.notificationservice.service.impl;

import com.banking.notificationservice.dto.NotificationDto;
import com.banking.notificationservice.entity.Notification;
import com.banking.notificationservice.repository.NotificationRepository;
import com.banking.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Notification Service - Author: Pratham Rathod | prathamrathod200@gmail.com
 */
@Service @RequiredArgsConstructor @Slf4j @Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public NotificationDto sendNotification(NotificationDto dto) {
        log.info("Sending {} notification to customer: {}", dto.getChannel(), dto.getCustomerId());
        Notification notification = Notification.builder()
                .customerId(dto.getCustomerId())
                .recipientEmail(dto.getRecipientEmail())
                .recipientPhone(dto.getRecipientPhone())
                .notificationType(dto.getNotificationType())
                .channel(dto.getChannel())
                .subject(dto.getSubject())
                .message(dto.getMessage())
                .status(Notification.NotificationStatus.PENDING)
                .referenceId(dto.getReferenceId())
                .build();

        try {
            // Simulate sending (in production, integrate with email/SMS provider)
            notification.setStatus(Notification.NotificationStatus.SENT);
            notification.setSentAt(LocalDateTime.now());
            log.info("Notification sent successfully to: {}", dto.getCustomerId());
        } catch (Exception e) {
            notification.setStatus(Notification.NotificationStatus.FAILED);
            notification.setFailureReason(e.getMessage());
            log.error("Failed to send notification: {}", e.getMessage());
        }

        return mapToDto(notificationRepository.save(notification));
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationDto> getNotificationsByCustomer(String customerId) {
        return notificationRepository.findByCustomerId(customerId).stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationDto> getNotificationsByCustomerPaged(String customerId, Pageable pageable) {
        return notificationRepository.findByCustomerId(customerId, pageable).map(this::mapToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationDto> getAllNotifications(Pageable pageable) {
        return notificationRepository.findAll(pageable).map(this::mapToDto);
    }

    private NotificationDto mapToDto(Notification n) {
        return NotificationDto.builder()
                .id(n.getId()).customerId(n.getCustomerId())
                .recipientEmail(n.getRecipientEmail()).recipientPhone(n.getRecipientPhone())
                .notificationType(n.getNotificationType()).channel(n.getChannel())
                .subject(n.getSubject()).message(n.getMessage())
                .status(n.getStatus()).referenceId(n.getReferenceId())
                .failureReason(n.getFailureReason())
                .createdAt(n.getCreatedAt()).sentAt(n.getSentAt()).build();
    }
}
