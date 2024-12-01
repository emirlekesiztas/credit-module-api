package com.emirhan.ingcasestudy.controller;

import com.emirhan.ingcasestudy.dto.CreateLoanRequest;
import com.emirhan.ingcasestudy.dto.LoanDTO;
import com.emirhan.ingcasestudy.dto.LoanPayRequest;
import com.emirhan.ingcasestudy.dto.LoanPayResponse;
import com.emirhan.ingcasestudy.service.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class LoanControllerTest {

    private MockMvc mockMvc;

    @Mock
    private LoanService loanService;

    @InjectMocks
    private LoanController loanController;

    private LoanDTO loanDTO;
    private LoanPayResponse loanPayResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(loanController).build();

        loanDTO = new LoanDTO();
        loanDTO.setId(1L);
        loanDTO.setCustomerId(1L);
        loanDTO.setLoanAmount(new BigDecimal("10000.00"));
        loanDTO.setLoanAmountWithInterestRate(new BigDecimal("11000.00"));
        loanDTO.setIsPaid(false);

        loanPayResponse = new LoanPayResponse();
        loanPayResponse.setLoanId(1L);
        loanPayResponse.setCustomerId(1L);
        loanPayResponse.setRemainingPaymentAmount(new BigDecimal("9000.00"));
        loanPayResponse.setIsLoanPaid(false);
        loanPayResponse.setTotalInstallmentPaid(1);
    }

    @Test
    void shouldCreateLoanSuccessfully() throws Exception {
        when(loanService.createLoan(any(CreateLoanRequest.class))).thenReturn(loanDTO);

        mockMvc.perform(post("/loan/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerId\":1,\"loanAmount\":10000.00}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.loanAmount").value(10000.00))
                .andExpect(jsonPath("$.loanAmountWithInterestRate").value(11000.00))
                .andExpect(jsonPath("$.customerId").value(1));
    }

    @Test
    void shouldPayLoanSuccessfully() throws Exception {
        when(loanService.payLoan(any(LoanPayRequest.class))).thenReturn(loanPayResponse);

        mockMvc.perform(post("/loan/pay")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"loanId\":1,\"amount\":1000.00}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loanId").value(1))
                .andExpect(jsonPath("$.remainingPaymentAmount").value(9000.00))
                .andExpect(jsonPath("$.isLoanPaid").value(false))
                .andExpect(jsonPath("$.totalInstallmentPaid").value(1));
    }

    @Test
    void shouldGetLoansByCustomerSuccessfully() throws Exception {
        when(loanService.getLoansByCustomer(1L, false)).thenReturn(Collections.singletonList(loanDTO));

        mockMvc.perform(get("/loan")
                        .param("customerId", "1")
                        .param("isPaid", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].loanAmount").value(10000.00))
                .andExpect(jsonPath("$[0].loanAmountWithInterestRate").value(11000.00))
                .andExpect(jsonPath("$[0].customerId").value(1))
                .andExpect(jsonPath("$[0].isPaid").value(false));
    }
}
