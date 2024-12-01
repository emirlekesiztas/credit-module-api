package com.emirhan.ingcasestudy.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "LOAN_INSTALLMENT")
@Data
public class LoanInstallment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "loan_id", nullable = false)
    private Loan loan;

    @Column
    private int installmentNr;

    @Column
    private BigDecimal amount;

    @Column
    private BigDecimal paidAmount;

    @Column
    private LocalDate dueDate;

    @Column
    private LocalDate paymentDate;

    @Column
    private Boolean isPaid;
}
