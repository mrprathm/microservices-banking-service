package com.banking.transactionservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Transaction Microservice
 * Author: Pratham Rathod | prathamrathod200@gmail.com | +91-9890394356 | Pune, Maharashtra
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@OpenAPIDefinition(info = @Info(title = "Transaction Service API", version = "1.0",
    description = "Microservices Banking System - Transaction Management",
    contact = @Contact(name = "Pratham Rathod", email = "prathamrathod200@gmail.com")))
public class TransactionServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(TransactionServiceApplication.class, args);
    }
}
