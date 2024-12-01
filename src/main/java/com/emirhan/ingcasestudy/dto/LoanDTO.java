package com.emirhan.ingcasestudy.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
public class LoanDTO {
    private Long id;
    private Long customerId;
    private String customerEmail;
    private String customerFullName;
    private BigDecimal loanAmount;
    private BigDecimal loanAmountWithInterestRate;
    private int numberOfInstallments;
    private Date createDate;
    private Boolean isPaid;

    public LoanDTO(long id, String customerFullName, BigDecimal loanAmount, int numberOfInstallments, BigDecimal loanAmountWithInterestRate) {
        this.id = id;
        this.customerFullName = customerFullName;
        this.loanAmount = loanAmount;
        this.numberOfInstallments = numberOfInstallments;
        this.loanAmountWithInterestRate = loanAmountWithInterestRate;
    }

    public LoanDTO(long id, BigDecimal loanAmount, boolean isPaid) {
        this.id = id;
        this.loanAmount = loanAmount;
        this.isPaid = isPaid;
    }
}
