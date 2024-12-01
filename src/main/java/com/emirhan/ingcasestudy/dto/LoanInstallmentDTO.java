package com.emirhan.ingcasestudy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanInstallmentDTO {
    private Long id;
    private Long loan;
    private int installmentNr;
    private BigDecimal amount;
    private BigDecimal paidAmount;
    private boolean isPaid;
    private LocalDate dueDate;
    private LocalDate paymentDate;

    public LoanInstallmentDTO(long id, BigDecimal amount, boolean isPaid, int installmentNr) {
        this.id = id;
        this.amount = amount;
        this.isPaid = isPaid;
        this.installmentNr = installmentNr;
    }
}
