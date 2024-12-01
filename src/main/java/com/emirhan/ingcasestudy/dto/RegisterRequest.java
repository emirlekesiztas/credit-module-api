package com.emirhan.ingcasestudy.dto;

import com.emirhan.ingcasestudy.entity.UserRole;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private BigDecimal creditLimit;
    private UserRole role;

    public RegisterRequest(String mail, String password, String firstName, String lastName) {
        this.email = mail;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
