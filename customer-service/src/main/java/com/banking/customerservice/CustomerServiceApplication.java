package com.banking.customerservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Customer Microservice - Manages all bank customer data
 * Author: Pratham Rathod
 * Email: prathamrathod200@gmail.com
 * Phone: +91-9890394356
 * Location: Pune, Maharashtra
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@OpenAPIDefinition(
    info = @Info(
        title = "Customer Service API",
        version = "1.0",
        description = "Microservices Banking System - Customer Management",
        contact = @Contact(name = "Pratham Rathod", email = "prathamrathod200@gmail.com")
    )
)
public class CustomerServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }
}
