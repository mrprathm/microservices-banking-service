package com.banking.notificationservice.repository;
import com.banking.notificationservice.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByCustomerId(String customerId);
    Page<Notification> findByCustomerId(String customerId, Pageable pageable);
    List<Notification> findByStatus(Notification.NotificationStatus status);
    long countByCustomerIdAndStatus(String customerId, Notification.NotificationStatus status);
}
