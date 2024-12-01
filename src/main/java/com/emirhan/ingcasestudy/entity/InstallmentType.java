package com.emirhan.ingcasestudy.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "INSTALLMENT_TYPE")
@Data
public class InstallmentType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private int installmentNr;
}
