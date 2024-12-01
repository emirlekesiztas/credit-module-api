package com.emirhan.ingcasestudy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanPayRequest {
    private Long loanId;
    private BigDecimal paidAmount;
    private Long customerId;

    public LoanPayRequest(long id, BigDecimal paidAmount) {
        this.loanId = id;
        this.paidAmount = paidAmount;
    }
}
