package com.banking.notificationservice.service;
import com.banking.notificationservice.dto.NotificationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface NotificationService {
    NotificationDto sendNotification(NotificationDto dto);
    List<NotificationDto> getNotificationsByCustomer(String customerId);
    Page<NotificationDto> getNotificationsByCustomerPaged(String customerId, Pageable pageable);
    Page<NotificationDto> getAllNotifications(Pageable pageable);
}
