package com.banking.customerservice.controller;

import com.banking.customerservice.dto.ApiResponse;
import com.banking.customerservice.dto.CustomerDto;
import com.banking.customerservice.entity.Customer;
import com.banking.customerservice.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Customer Controller - REST APIs for Customer Management
 * Author: Pratham Rathod | prathamrathod200@gmail.com | +91-9890394356
 */
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Customer Management", description = "APIs for managing bank customers")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    @Operation(summary = "Register new customer", description = "Creates a new bank customer with KYC details")
    public ResponseEntity<ApiResponse<CustomerDto>> createCustomer(@Valid @RequestBody CustomerDto customerDto) {
        log.info("REST request to create customer: {}", customerDto.getEmail());
        CustomerDto created = customerService.createCustomer(customerDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Customer registered successfully", created));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get customer by ID")
    public ResponseEntity<ApiResponse<CustomerDto>> getCustomerById(@PathVariable Long id) {
        CustomerDto customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(ApiResponse.success("Customer fetched successfully", customer));
    }

    @GetMapping("/customer-id/{customerId}")
    @Operation(summary = "Get customer by Customer ID")
    public ResponseEntity<ApiResponse<CustomerDto>> getCustomerByCustomerId(@PathVariable String customerId) {
        CustomerDto customer = customerService.getCustomerByCustomerId(customerId);
        return ResponseEntity.ok(ApiResponse.success("Customer fetched successfully", customer));
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Get customer by email")
    public ResponseEntity<ApiResponse<CustomerDto>> getCustomerByEmail(@PathVariable String email) {
        CustomerDto customer = customerService.getCustomerByEmail(email);
        return ResponseEntity.ok(ApiResponse.success("Customer fetched successfully", customer));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update customer details")
    public ResponseEntity<ApiResponse<CustomerDto>> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerDto customerDto) {
        CustomerDto updated = customerService.updateCustomer(id, customerDto);
        return ResponseEntity.ok(ApiResponse.success("Customer updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deactivate customer")
    public ResponseEntity<ApiResponse<Void>> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok(ApiResponse.success("Customer deactivated successfully", null));
    }

    @GetMapping
    @Operation(summary = "Get all customers with pagination")
    public ResponseEntity<ApiResponse<Page<CustomerDto>>> getAllCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<CustomerDto> customers = customerService.getAllCustomers(pageable);
        return ResponseEntity.ok(ApiResponse.success("Customers fetched successfully", customers));
    }

    @GetMapping("/search")
    @Operation(summary = "Search customers")
    public ResponseEntity<ApiResponse<Page<CustomerDto>>> searchCustomers(
            @Parameter(description = "Search query (name, email, phone)") @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CustomerDto> customers = customerService.searchCustomers(query, pageable);
        return ResponseEntity.ok(ApiResponse.success("Search results fetched", customers));
    }

    @PatchMapping("/{customerId}/kyc-status")
    @Operation(summary = "Update KYC status")
    public ResponseEntity<ApiResponse<CustomerDto>> updateKycStatus(
            @PathVariable String customerId,
            @RequestParam Customer.KycStatus kycStatus) {
        CustomerDto updated = customerService.updateKycStatus(customerId, kycStatus);
        return ResponseEntity.ok(ApiResponse.success("KYC status updated", updated));
    }

    @PatchMapping("/{customerId}/status")
    @Operation(summary = "Update customer account status")
    public ResponseEntity<ApiResponse<CustomerDto>> updateCustomerStatus(
            @PathVariable String customerId,
            @RequestParam Customer.CustomerStatus status) {
        CustomerDto updated = customerService.updateCustomerStatus(customerId, status);
        return ResponseEntity.ok(ApiResponse.success("Customer status updated", updated));
    }
}
