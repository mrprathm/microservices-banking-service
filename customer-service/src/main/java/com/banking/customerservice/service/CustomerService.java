package com.banking.customerservice.service;

import com.banking.customerservice.dto.CustomerDto;
import com.banking.customerservice.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {

    CustomerDto createCustomer(CustomerDto customerDto);

    CustomerDto getCustomerById(Long id);

    CustomerDto getCustomerByCustomerId(String customerId);

    CustomerDto getCustomerByEmail(String email);

    CustomerDto updateCustomer(Long id, CustomerDto customerDto);

    void deleteCustomer(Long id);

    Page<CustomerDto> getAllCustomers(Pageable pageable);

    Page<CustomerDto> searchCustomers(String query, Pageable pageable);

    CustomerDto updateKycStatus(String customerId, Customer.KycStatus kycStatus);

    CustomerDto updateCustomerStatus(String customerId, Customer.CustomerStatus status);
}
