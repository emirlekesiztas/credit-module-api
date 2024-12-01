package com.emirhan.ingcasestudy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanPayResponse {
    private Long loanId;
    private Long customerId;
    private BigDecimal remainingPaymentAmount;
    private BigDecimal refundAmount;
    private Boolean isLoanPaid;
    private int totalInstallmentPaid;
}
