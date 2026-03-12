package com.banking.loanservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Loan Microservice - Author: Pratham Rathod | prathamrathod200@gmail.com | Pune, Maharashtra
 */
@SpringBootApplication @EnableDiscoveryClient @EnableFeignClients
@OpenAPIDefinition(info = @Info(title = "Loan Service API", version = "1.0",
    description = "Microservices Banking System - Loan Management",
    contact = @Contact(name = "Pratham Rathod", email = "prathamrathod200@gmail.com")))
public class LoanServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(LoanServiceApplication.class, args);
    }
}
