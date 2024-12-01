package com.emirhan.ingcasestudy.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateLoanRequest {
    @NotNull(message = "Customer ID cannot be null")
    private Long customerId;

    @NotNull(message = "Loan amount cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Loan amount must be greater than 0")
    private BigDecimal loanAmount;

    @NotNull(message = "Number of installments cannot be null")
    private int numberOfInstallments;
}
