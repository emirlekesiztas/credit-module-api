package com.emirhan.ingcasestudy.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "LOAN")
@Data
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private User ownedBy;

    @Column
    private BigDecimal loanAmount;

    @Column
    private BigDecimal loanAmountWithInterestRate;

    @Column
    private int numberOfInstallments;

    @Column
    private Date createDate;

    @Column
    private Boolean isPaid;

    public Loan() {
        setIsPaid(false);
    }


    public Loan(Long id) {
        this.id = id;
    }
}
