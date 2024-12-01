package com.emirhan.ingcasestudy.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "INTEREST_RATE")
@Data
@NoArgsConstructor
public class InterestRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Date addedOn;
    @Column
    private BigDecimal interestRate;

    public InterestRate(BigDecimal interestRate) {
        addedOn = new Date();
        this.interestRate = interestRate;
    }
}
