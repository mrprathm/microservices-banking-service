package com.banking.notificationservice.controller;

import com.banking.notificationservice.dto.ApiResponse;
import com.banking.notificationservice.dto.NotificationDto;
import com.banking.notificationservice.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Notification Controller - Author: Pratham Rathod
 */
@RestController @RequestMapping("/api/v1/notifications") @RequiredArgsConstructor
@Tag(name = "Notification Management", description = "APIs for managing notifications")
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping("/send")
    @Operation(summary = "Send notification to customer")
    public ResponseEntity<ApiResponse<NotificationDto>> sendNotification(@Valid @RequestBody NotificationDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Notification sent", notificationService.sendNotification(dto)));
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get all notifications for customer")
    public ResponseEntity<ApiResponse<List<NotificationDto>>> getByCustomer(@PathVariable String customerId) {
        return ResponseEntity.ok(ApiResponse.success("Notifications fetched", notificationService.getNotificationsByCustomer(customerId)));
    }

    @GetMapping("/customer/{customerId}/paged")
    @Operation(summary = "Get paginated notifications for customer")
    public ResponseEntity<ApiResponse<Page<NotificationDto>>> getByCustomerPaged(
            @PathVariable String customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success("Notifications fetched",
                notificationService.getNotificationsByCustomerPaged(customerId, PageRequest.of(page, size, Sort.by("createdAt").descending()))));
    }

    @GetMapping
    @Operation(summary = "Get all notifications (admin)")
    public ResponseEntity<ApiResponse<Page<NotificationDto>>> getAllNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success("All notifications fetched",
                notificationService.getAllNotifications(PageRequest.of(page, size, Sort.by("createdAt").descending()))));
    }
}
