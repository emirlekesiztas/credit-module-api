package com.emirhan.ingcasestudy.controller;

import com.emirhan.ingcasestudy.dto.LoanInstallmentDTO;
import com.emirhan.ingcasestudy.service.LoanInstallmentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/loan-installment")
@RequiredArgsConstructor
public class LoanInstallmentController {
    private final LoanInstallmentService loanInstallmentService;


    @GetMapping("/list")
    @Operation(summary = "Get Loan Installments By Loan", description = "This endpoint returns loan installments by loan id and with other filters.")
    public ResponseEntity<List<LoanInstallmentDTO>> getLoanInstallmentsByLoan(@RequestParam Long loanId,
                                                                              @Nullable @RequestParam(required = false) Boolean isPaid) {
        return ResponseEntity.ok(loanInstallmentService.getLoanInstallmentsByLoan(loanId, isPaid));
    }
}
