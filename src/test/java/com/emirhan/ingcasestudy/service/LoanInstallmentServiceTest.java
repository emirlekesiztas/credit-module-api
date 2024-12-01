package com.emirhan.ingcasestudy.service;

import com.emirhan.ingcasestudy.dto.LoanInstallmentDTO;
import com.emirhan.ingcasestudy.entity.Loan;
import com.emirhan.ingcasestudy.entity.LoanInstallment;
import com.emirhan.ingcasestudy.entity.User;
import com.emirhan.ingcasestudy.entity.UserRole;
import com.emirhan.ingcasestudy.mapper.LoanInstallmentMapper;
import com.emirhan.ingcasestudy.repository.LoanInstallmentRepository;
import com.emirhan.ingcasestudy.repository.LoanRepository;
import com.emirhan.ingcasestudy.util.AuthUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanInstallmentServiceTest {

    @Mock
    private LoanInstallmentRepository loanInstallmentRepository;

    @Mock
    private LoanInstallmentMapper loanInstallmentMapper;

    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private LoanInstallmentService loanInstallmentService;

    @Test
    void testGetLoanInstallmentsByLoan_AsAdmin_Success() {
        User admin = new User();
        admin.setId(1L);
        admin.setRole(UserRole.ADMIN);

        User customer = new User();
        customer.setId(2L);

        Loan loan = new Loan();
        loan.setId(1L);
        loan.setOwnedBy(customer);

        LoanInstallment installment = new LoanInstallment();
        installment.setInstallmentNr(1);
        installment.setAmount(new BigDecimal("500"));
        installment.setIsPaid(false);
        installment.setInstallmentNr(1);
        installment.setDueDate(LocalDate.now().plusMonths(1));

        List<LoanInstallment> installments = List.of(installment);

        try (MockedStatic<AuthUtil> mockedAuthUtil = mockStatic(AuthUtil.class)) {
            mockedAuthUtil.when(AuthUtil::getCurrentUserRole).thenReturn("ADMIN");

            when(loanRepository.findById(eq(1L))).thenReturn(Optional.of(loan));
            when(loanInstallmentRepository.findAllLoansByLoanAndFilters(eq(1L), eq(false))).thenReturn(installments);
            when(loanInstallmentMapper.toLoanInstallmentDTOList(installments))
                    .thenReturn(List.of(new LoanInstallmentDTO(1L, new BigDecimal("500"), false, 1)));

            List<LoanInstallmentDTO> result = loanInstallmentService.getLoanInstallmentsByLoan(1L, false);

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(1, result.get(0).getInstallmentNr());
        }
    }

    @Test
    void testGetLoanInstallmentsByLoan_AsCustomer_Failure() {
        User customer = new User();
        customer.setId(2L);
        customer.setRole(UserRole.CUSTOMER);

        User otherUser = new User();
        otherUser.setId(3L);

        Loan loan = new Loan();
        loan.setId(1L);
        loan.setOwnedBy(otherUser);

        try (MockedStatic<AuthUtil> mockedAuthUtil = mockStatic(AuthUtil.class)) {
            mockedAuthUtil.when(AuthUtil::getCurrentUserRole).thenReturn("CUSTOMER");
            mockedAuthUtil.when(AuthUtil::getCurrentUserId).thenReturn(2L);

            when(loanRepository.findById(eq(1L))).thenReturn(Optional.of(loan));

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                loanInstallmentService.getLoanInstallmentsByLoan(1L, false);
            });
            assertEquals("Customers can only list their own loan installments", exception.getMessage());
        }
    }
}