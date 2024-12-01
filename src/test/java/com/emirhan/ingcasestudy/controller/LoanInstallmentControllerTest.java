package com.emirhan.ingcasestudy.controller;

import com.emirhan.ingcasestudy.dto.LoanInstallmentDTO;
import com.emirhan.ingcasestudy.service.LoanInstallmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class LoanInstallmentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private LoanInstallmentService loanInstallmentService;

    @InjectMocks
    private LoanInstallmentController loanInstallmentController;

    private LoanInstallmentDTO loanInstallmentDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(loanInstallmentController).build();

        loanInstallmentDTO = new LoanInstallmentDTO();
        loanInstallmentDTO.setId(1L);
        loanInstallmentDTO.setLoan(1L);
        loanInstallmentDTO.setAmount(new BigDecimal("500.00"));
        loanInstallmentDTO.setPaid(false);
    }

    @Test
    void shouldGetLoanInstallmentsByLoanSuccessfully() throws Exception {
        when(loanInstallmentService.getLoanInstallmentsByLoan(eq(1L), eq(false)))
                .thenReturn(Collections.singletonList(loanInstallmentDTO));

        mockMvc.perform(get("/loan-installment/list")
                        .param("loanId", "1")
                        .param("isPaid", "false")
                        .param("isOverdue", "false")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].loanId").value(1))
                .andExpect(jsonPath("$[0].amount").value(500.00))
                .andExpect(jsonPath("$[0].paid").value(false));
    }

    @Test
    void shouldGetLoanInstallmentsByLoanWithoutOptionalParams() throws Exception {
        when(loanInstallmentService.getLoanInstallmentsByLoan(eq(1L), eq(null)))
                .thenReturn(Arrays.asList(loanInstallmentDTO));

        mockMvc.perform(get("/loan-installment/list")
                        .param("loanId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].loanId").value(1))
                .andExpect(jsonPath("$[0].amount").value(500.00))
                .andExpect(jsonPath("$[0].paid").value(false));
    }
}
