package com.banking.loanservice.controller;

import com.banking.loanservice.dto.ApiResponse;
import com.banking.loanservice.dto.LoanDto;
import com.banking.loanservice.service.LoanService;
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

import java.math.BigDecimal;
import java.util.List;

/**
 * Loan Controller - Author: Pratham Rathod | prathamrathod200@gmail.com
 */
@RestController
@RequestMapping("/api/v1/loans")
@RequiredArgsConstructor
@Tag(name = "Loan Management", description = "APIs for managing bank loans")
public class LoanController {

    private final LoanService loanService;

    @PostMapping("/apply")
    @Operation(summary = "Apply for a new loan")
    public ResponseEntity<ApiResponse<LoanDto>> applyForLoan(@Valid @RequestBody LoanDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Loan application submitted", loanService.applyForLoan(dto)));
    }

    @PutMapping("/{loanNumber}/approve")
    @Operation(summary = "Approve loan application")
    public ResponseEntity<ApiResponse<LoanDto>> approveLoan(@PathVariable String loanNumber) {
        return ResponseEntity.ok(ApiResponse.success("Loan approved", loanService.approveLoan(loanNumber)));
    }

    @PutMapping("/{loanNumber}/reject")
    @Operation(summary = "Reject loan application")
    public ResponseEntity<ApiResponse<LoanDto>> rejectLoan(@PathVariable String loanNumber, @RequestParam String reason) {
        return ResponseEntity.ok(ApiResponse.success("Loan rejected", loanService.rejectLoan(loanNumber, reason)));
    }

    @PutMapping("/{loanNumber}/disburse")
    @Operation(summary = "Disburse approved loan")
    public ResponseEntity<ApiResponse<LoanDto>> disburseLoan(@PathVariable String loanNumber) {
        return ResponseEntity.ok(ApiResponse.success("Loan disbursed successfully", loanService.disburseLoan(loanNumber)));
    }

    @PostMapping("/{loanNumber}/pay-emi")
    @Operation(summary = "Pay EMI for active loan")
    public ResponseEntity<ApiResponse<LoanDto>> payEMI(@PathVariable String loanNumber, @RequestParam BigDecimal amount) {
        return ResponseEntity.ok(ApiResponse.success("EMI paid successfully", loanService.payEMI(loanNumber, amount)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get loan by ID")
    public ResponseEntity<ApiResponse<LoanDto>> getLoanById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Loan fetched", loanService.getLoanById(id)));
    }

    @GetMapping("/loan-number/{loanNumber}")
    @Operation(summary = "Get loan by loan number")
    public ResponseEntity<ApiResponse<LoanDto>> getLoanByNumber(@PathVariable String loanNumber) {
        return ResponseEntity.ok(ApiResponse.success("Loan fetched", loanService.getLoanByLoanNumber(loanNumber)));
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get all loans for a customer")
    public ResponseEntity<ApiResponse<List<LoanDto>>> getByCustomer(@PathVariable String customerId) {
        return ResponseEntity.ok(ApiResponse.success("Loans fetched", loanService.getLoansByCustomerId(customerId)));
    }

    @GetMapping("/customer/{customerId}/total-outstanding")
    @Operation(summary = "Get total outstanding loan amount")
    public ResponseEntity<ApiResponse<BigDecimal>> getTotalOutstanding(@PathVariable String customerId) {
        return ResponseEntity.ok(ApiResponse.success("Total outstanding fetched", loanService.getTotalOutstandingByCustomer(customerId)));
    }

    @GetMapping("/calculate-emi")
    @Operation(summary = "Calculate EMI before applying")
    public ResponseEntity<ApiResponse<BigDecimal>> calculateEMI(
            @RequestParam BigDecimal principal,
            @RequestParam BigDecimal annualRate,
            @RequestParam int tenureMonths) {
        BigDecimal emi = loanService.calculateEMI(principal, annualRate, tenureMonths);
        return ResponseEntity.ok(ApiResponse.success("EMI calculated successfully", emi));
    }

    @GetMapping
    @Operation(summary = "Get all loans (admin)")
    public ResponseEntity<ApiResponse<Page<LoanDto>>> getAllLoans(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success("Loans fetched",
                loanService.getAllLoans(PageRequest.of(page, size, Sort.by("appliedAt").descending()))));
    }
}
