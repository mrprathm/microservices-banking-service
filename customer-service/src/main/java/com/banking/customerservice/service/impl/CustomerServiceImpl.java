package com.banking.customerservice.service.impl;

import com.banking.customerservice.dto.CustomerDto;
import com.banking.customerservice.entity.Customer;
import com.banking.customerservice.exception.CustomerAlreadyExistsException;
import com.banking.customerservice.exception.CustomerNotFoundException;
import com.banking.customerservice.repository.CustomerRepository;
import com.banking.customerservice.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Customer Service Implementation
 * Author: Pratham Rathod | Pune, Maharashtra
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public CustomerDto createCustomer(CustomerDto customerDto) {
        log.info("Creating new customer with email: {}", customerDto.getEmail());

        // Validate unique fields
        if (customerRepository.existsByEmail(customerDto.getEmail())) {
            throw new CustomerAlreadyExistsException("Customer already exists with email: " + customerDto.getEmail());
        }
        if (customerRepository.existsByPhoneNumber(customerDto.getPhoneNumber())) {
            throw new CustomerAlreadyExistsException("Customer already exists with phone: " + customerDto.getPhoneNumber());
        }
        if (customerRepository.existsByPanNumber(customerDto.getPanNumber())) {
            throw new CustomerAlreadyExistsException("Customer already exists with PAN: " + customerDto.getPanNumber());
        }
        if (customerRepository.existsByAadhaarNumber(customerDto.getAadhaarNumber())) {
            throw new CustomerAlreadyExistsException("Customer already exists with Aadhaar: " + customerDto.getAadhaarNumber());
        }

        Customer customer = mapToEntity(customerDto);
        customer.setCustomerId(generateCustomerId());
        customer.setStatus(Customer.CustomerStatus.ACTIVE);
        customer.setKycStatus(Customer.KycStatus.PENDING);
        customer.setCreatedBy("SYSTEM");

        Customer saved = customerRepository.save(customer);
        log.info("Customer created successfully with ID: {}", saved.getCustomerId());
        return mapToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerDto getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
        return mapToDto(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerDto getCustomerByCustomerId(String customerId) {
        Customer customer = customerRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with customerId: " + customerId));
        return mapToDto(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerDto getCustomerByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with email: " + email));
        return mapToDto(customer);
    }

    @Override
    public CustomerDto updateCustomer(Long id, CustomerDto customerDto) {
        log.info("Updating customer with id: {}", id);
        Customer existing = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));

        existing.setFirstName(customerDto.getFirstName());
        existing.setLastName(customerDto.getLastName());
        existing.setAddress(customerDto.getAddress());
        existing.setCity(customerDto.getCity());
        existing.setState(customerDto.getState());
        existing.setPincode(customerDto.getPincode());
        existing.setOccupation(customerDto.getOccupation());
        existing.setAnnualIncome(customerDto.getAnnualIncome());

        Customer updated = customerRepository.save(existing);
        log.info("Customer updated successfully: {}", updated.getCustomerId());
        return mapToDto(updated);
    }

    @Override
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
        customer.setStatus(Customer.CustomerStatus.INACTIVE);
        customerRepository.save(customer);
        log.info("Customer deactivated: {}", customer.getCustomerId());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerDto> getAllCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable).map(this::mapToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerDto> searchCustomers(String query, Pageable pageable) {
        return customerRepository.searchCustomers(query, pageable).map(this::mapToDto);
    }

    @Override
    public CustomerDto updateKycStatus(String customerId, Customer.KycStatus kycStatus) {
        Customer customer = customerRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found: " + customerId));
        customer.setKycStatus(kycStatus);
        return mapToDto(customerRepository.save(customer));
    }

    @Override
    public CustomerDto updateCustomerStatus(String customerId, Customer.CustomerStatus status) {
        Customer customer = customerRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found: " + customerId));
        customer.setStatus(status);
        return mapToDto(customerRepository.save(customer));
    }

    private String generateCustomerId() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();
        return "CUST" + timestamp + uuid;
    }

    private Customer mapToEntity(CustomerDto dto) {
        return Customer.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .panNumber(dto.getPanNumber())
                .aadhaarNumber(dto.getAadhaarNumber())
                .dateOfBirth(dto.getDateOfBirth())
                .gender(dto.getGender())
                .address(dto.getAddress())
                .city(dto.getCity())
                .state(dto.getState())
                .pincode(dto.getPincode())
                .occupation(dto.getOccupation())
                .annualIncome(dto.getAnnualIncome())
                .build();
    }

    private CustomerDto mapToDto(Customer customer) {
        return CustomerDto.builder()
                .id(customer.getId())
                .customerId(customer.getCustomerId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .phoneNumber(customer.getPhoneNumber())
                .panNumber(customer.getPanNumber())
                .aadhaarNumber(customer.getAadhaarNumber())
                .dateOfBirth(customer.getDateOfBirth())
                .gender(customer.getGender())
                .address(customer.getAddress())
                .city(customer.getCity())
                .state(customer.getState())
                .pincode(customer.getPincode())
                .status(customer.getStatus())
                .kycStatus(customer.getKycStatus())
                .occupation(customer.getOccupation())
                .annualIncome(customer.getAnnualIncome())
                .createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdatedAt())
                .build();
    }
}
