package com.emirhan.ingcasestudy.controller;

import com.emirhan.ingcasestudy.dto.CreateLoanRequest;
import com.emirhan.ingcasestudy.dto.LoanDTO;
import com.emirhan.ingcasestudy.dto.LoanPayRequest;
import com.emirhan.ingcasestudy.dto.LoanPayResponse;
import com.emirhan.ingcasestudy.service.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loan")
@RequiredArgsConstructor
public class LoanController {
    private final LoanService loanService;


    @PostMapping("/create")
    public ResponseEntity<LoanDTO> createLoan(@Valid @RequestBody CreateLoanRequest request) {
        return ResponseEntity.ok(loanService.createLoan(request));
    }

    @PostMapping("/pay")
    @Operation(summary = "Pay Loan Installment By Id", description = "This endpoint pays loan installments by loan id and customer id.")
    public ResponseEntity<LoanPayResponse> payLoanInstallmentByLoanId(@RequestBody LoanPayRequest payRequest) {
        return ResponseEntity.ok(loanService.payLoan(payRequest));
    }

    @GetMapping
    @Operation(summary = "Get Loans By Customer Id", description = "This endpoint returns loans by customer id.")
    public ResponseEntity<List<LoanDTO>> getLoansByCustomerId(@RequestParam Long customerId,@RequestParam Boolean isPaid) {
        return ResponseEntity.ok(loanService.getLoansByCustomer(customerId, isPaid));
    }
}
