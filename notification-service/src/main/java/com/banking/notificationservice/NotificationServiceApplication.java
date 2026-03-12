package com.banking.notificationservice;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
/**
 * Notification Microservice - Author: Pratham Rathod | prathamrathod200@gmail.com | Pune, Maharashtra
 */
@SpringBootApplication @EnableDiscoveryClient
public class NotificationServiceApplication {
    public static void main(String[] args) { SpringApplication.run(NotificationServiceApplication.class, args); }
}
