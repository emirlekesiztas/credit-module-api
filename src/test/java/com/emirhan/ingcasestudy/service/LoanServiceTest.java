package com.emirhan.ingcasestudy.service;

import com.emirhan.ingcasestudy.dto.CreateLoanRequest;
import com.emirhan.ingcasestudy.dto.LoanDTO;
import com.emirhan.ingcasestudy.dto.LoanPayRequest;
import com.emirhan.ingcasestudy.dto.LoanPayResponse;
import com.emirhan.ingcasestudy.entity.*;
import com.emirhan.ingcasestudy.mapper.LoanMapper;
import com.emirhan.ingcasestudy.repository.*;
import com.emirhan.ingcasestudy.util.AuthUtil;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class LoanServiceTest {

    @InjectMocks
    private LoanService loanService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private InterestRateRepository interestRateRepository;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private LoanMapper loanMapper;

    @Mock
    private InstallmentTypeRepository installmentTypeRepository;

    @Mock
    private LoanInstallmentRepository loanInstallmentRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateLoan_Success() {
        User customer = new User();
        customer.setId(1L);
        customer.setCreditLimit(new BigDecimal("5000"));
        InterestRate interestRate = new InterestRate(new BigDecimal("0.3"));
        CreateLoanRequest request = new CreateLoanRequest(1L, new BigDecimal("1000"), 12);

        Loan loan = new Loan();
        loan.setId(1L);
        loan.setLoanAmount(new BigDecimal("1000"));
        loan.setNumberOfInstallments(12);
        loan.setLoanAmountWithInterestRate(new BigDecimal("1300.00"));

        LoanDTO expectedLoanDTO = new LoanDTO(1L, "Emirhan Lekesiztaş", new BigDecimal("1000"), 12, new BigDecimal("1300"));

        try (MockedStatic<AuthUtil> mockedAuthUtil = mockStatic(AuthUtil.class)) {
            mockedAuthUtil.when(AuthUtil::getCurrentUserRole).thenReturn("ROLE_USER");

            when(userRepository.findById(1L)).thenReturn(Optional.of(customer));
            when(interestRateRepository.findByAddedOnYear(anyInt())).thenReturn(Optional.of(interestRate));
            when(installmentTypeRepository.existsByInstallmentNr(anyInt())).thenReturn(true);
            when(loanInstallmentRepository.saveAll(anyList())).thenReturn(new ArrayList<>());
            when(loanRepository.save(any(Loan.class))).thenAnswer(invocation -> {
                Loan passedLoan = invocation.getArgument(0);
                passedLoan.setId(1L);
                return passedLoan;
            });
            when(loanMapper.toLoanDTO(any(Loan.class)))
                    .thenAnswer(invocation -> {
                        Loan passedLoan = invocation.getArgument(0);
                        return new LoanDTO(
                                passedLoan.getId(),
                                "Emirhan lekesiztaş",
                                passedLoan.getLoanAmount(),
                                passedLoan.getNumberOfInstallments(),
                                passedLoan.getLoanAmountWithInterestRate()
                        );
                    });

            LoanDTO result = loanService.createLoan(request);

            assertNotNull(result);
            assertEquals(expectedLoanDTO.getId(), result.getId());
            assertEquals(expectedLoanDTO.getNumberOfInstallments(), result.getNumberOfInstallments());
            assertThat(result.getLoanAmountWithInterestRate().compareTo(expectedLoanDTO.getLoanAmountWithInterestRate())).isZero();
        }
    }

    @Test
    void testCreateLoan_AsAdmin_Success() {
        User customer = new User();
        customer.setId(1L);
        customer.setCreditLimit(new BigDecimal("5000"));
        customer.setRole(UserRole.ADMIN);
        InterestRate interestRate = new InterestRate(new BigDecimal("0.3"));
        CreateLoanRequest request = new CreateLoanRequest(1L, new BigDecimal("1000"), 12);

        Loan loan = new Loan();
        loan.setId(1L);
        loan.setLoanAmount(new BigDecimal("1000"));
        loan.setNumberOfInstallments(12);
        loan.setLoanAmountWithInterestRate(new BigDecimal("1300"));

        LoanDTO expectedLoanDTO = new LoanDTO(1L, "Emirhan Lekesiztaş", new BigDecimal("1000"), 12, new BigDecimal("1300"));

        try (MockedStatic<AuthUtil> mockedAuthUtil = mockStatic(AuthUtil.class)) {
            mockedAuthUtil.when(AuthUtil::getCurrentUserRole).thenReturn("ADMIN");

            when(userRepository.findById(1L)).thenReturn(Optional.of(customer));
            when(interestRateRepository.findByAddedOnYear(anyInt())).thenReturn(Optional.of(interestRate));
            when(installmentTypeRepository.existsByInstallmentNr(anyInt())).thenReturn(true);
            when(loanInstallmentRepository.saveAll(anyList())).thenReturn(new ArrayList<>());
            when(loanRepository.save(any(Loan.class))).thenAnswer(invocation -> {
                Loan passedLoan = invocation.getArgument(0);
                passedLoan.setId(1L);
                return passedLoan;
            });
            when(loanMapper.toLoanDTO(any(Loan.class)))
                    .thenAnswer(invocation -> {
                        Loan passedLoan = invocation.getArgument(0);
                        return new LoanDTO(
                                passedLoan.getId(),
                                "Emirhan Lekesiztaş",
                                passedLoan.getLoanAmount(),
                                passedLoan.getNumberOfInstallments(),
                                passedLoan.getLoanAmountWithInterestRate()
                        );
                    });

            LoanDTO result = loanService.createLoan(request);

            assertNotNull(result);
            assertEquals(expectedLoanDTO.getId(), result.getId());
            assertEquals(expectedLoanDTO.getNumberOfInstallments(), result.getNumberOfInstallments());
            assertThat(result.getLoanAmountWithInterestRate().compareTo(expectedLoanDTO.getLoanAmountWithInterestRate())).isZero();
        }
    }

    @Test
    void testCreateLoan_AsCustomer_CreatingForOthers_Failure() {
        User customer = new User();
        customer.setId(2L);
        customer.setCreditLimit(new BigDecimal("5000"));
        customer.setRole(UserRole.CUSTOMER);

        InterestRate interestRate = new InterestRate(new BigDecimal("0.3"));
        CreateLoanRequest request = new CreateLoanRequest(1L, new BigDecimal("1000"), 12);

        Loan loan = new Loan();
        loan.setId(1L);
        loan.setLoanAmount(new BigDecimal("1000"));
        loan.setNumberOfInstallments(12);
        loan.setLoanAmountWithInterestRate(new BigDecimal("1300"));

        try (MockedStatic<AuthUtil> mockedAuthUtil = mockStatic(AuthUtil.class)) {
            mockedAuthUtil.when(AuthUtil::getCurrentUserRole).thenReturn("CUSTOMER");

            when(userRepository.findById(1L)).thenReturn(Optional.of(customer));
            when(interestRateRepository.findByAddedOnYear(anyInt())).thenReturn(Optional.of(interestRate));
            when(installmentTypeRepository.existsByInstallmentNr(anyInt())).thenReturn(true);
            when(loanRepository.save(any(Loan.class))).thenAnswer(invocation -> {
                Loan passedLoan = invocation.getArgument(0);
                passedLoan.setId(1L);
                return passedLoan;
            });
            RuntimeException exception = assertThrows(IllegalArgumentException.class, () -> loanService.createLoan(request));
            assertEquals("Customers can only create their own loans", exception.getMessage());
        }
    }


    @Test
    void testCreateLoan_UserNotFound() {
        CreateLoanRequest request = new CreateLoanRequest(1L, new BigDecimal("1000"), 12);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> loanService.createLoan(request));
    }

    @Test
    void testCreateLoan_InterestRateNotFound() {
        User customer = new User();
        customer.setId(1L);
        CreateLoanRequest request = new CreateLoanRequest(1L, new BigDecimal("1000"), 12);

        when(userRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(interestRateRepository.findByAddedOnYear(anyInt())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> loanService.createLoan(request));
    }


    @Test
    void testPayLoan_AsAdmin_Success() {
        User admin = new User();
        admin.setId(1L);
        admin.setRole(UserRole.ADMIN);

        User customer = new User();
        customer.setId(2L);

        Loan loan = new Loan();
        loan.setId(1L);
        loan.setOwnedBy(customer);
        loan.setLoanAmountWithInterestRate(new BigDecimal("1000"));
        loan.setIsPaid(false);

        LoanInstallment installment = new LoanInstallment();
        installment.setInstallmentNr(1);
        installment.setAmount(new BigDecimal("500"));
        installment.setIsPaid(false);
        installment.setDueDate(LocalDate.now().plusMonths(1));
        List<LoanInstallment> installments = List.of(installment);

        LoanPayRequest request = new LoanPayRequest(1L, new BigDecimal("500"));

        try (MockedStatic<AuthUtil> mockedAuthUtil = mockStatic(AuthUtil.class)) {
            mockedAuthUtil.when(AuthUtil::getCurrentUserRole).thenReturn("ROLE_ADMIN");

            when(loanRepository.findById(eq(1L))).thenReturn(Optional.of(loan));

            when(userRepository.findById(eq(1L))).thenReturn(Optional.of(admin));
            when(userRepository.findById(eq(2L))).thenReturn(Optional.of(customer));

            when(loanInstallmentRepository.findUnpaidLoanInstallments(eq(1L))).thenReturn(installments);

            LoanPayResponse response = loanService.payLoan(request);

            assertNotNull(response);
            assertEquals(new BigDecimal("0"), response.getRefundAmount());
            assertEquals(1, response.getTotalInstallmentPaid());
            assertTrue(installment.getIsPaid());
        }
    }


    @Test
    void testPayLoan_AsCustomer_Failure() {
        User customer = new User();
        customer.setId(2L);
        customer.setRole(UserRole.CUSTOMER);

        User otherUser = new User();
        otherUser.setId(3L);

        Loan loan = new Loan();
        loan.setId(1L);
        loan.setOwnedBy(otherUser);
        loan.setLoanAmountWithInterestRate(new BigDecimal("1000"));
        loan.setIsPaid(false);

        LoanInstallment installment = new LoanInstallment();
        installment.setInstallmentNr(1);
        installment.setAmount(new BigDecimal("500"));
        installment.setIsPaid(false);
        installment.setDueDate(LocalDate.now().plusMonths(1));
        List<LoanInstallment> installments = List.of(installment);

        LoanPayRequest request = new LoanPayRequest(1L, new BigDecimal("500"));

        try (MockedStatic<AuthUtil> mockedAuthUtil = mockStatic(AuthUtil.class)) {
            mockedAuthUtil.when(AuthUtil::getCurrentUserRole).thenReturn("CUSTOMER");

            when(loanRepository.findById(eq(1L))).thenReturn(Optional.of(loan));

            when(userRepository.findById(eq(2L))).thenReturn(Optional.of(customer));
            when(userRepository.findById(eq(3L))).thenReturn(Optional.of(otherUser));

            when(loanInstallmentRepository.findUnpaidLoanInstallments(eq(1L))).thenReturn(installments);

            RuntimeException exception = assertThrows(IllegalArgumentException.class, () -> loanService.payLoan(request));
            assertEquals("Customers can only pay for their own loans", exception.getMessage());
        }
    }


    @Test
    void testGetLoansByCustomer_AsAdmin_Success() {
        User admin = new User();
        admin.setId(1L);
        admin.setRole(UserRole.ADMIN);

        User customer = new User();
        customer.setId(2L);

        Loan loan = new Loan();
        loan.setId(1L);
        loan.setOwnedBy(customer);

        List<Loan> loans = List.of(loan);

        try (MockedStatic<AuthUtil> mockedAuthUtil = mockStatic(AuthUtil.class)) {
            mockedAuthUtil.when(AuthUtil::getCurrentUserRole).thenReturn("ADMIN");

            when(userRepository.findById(eq(1L))).thenReturn(Optional.of(admin));
            when(userRepository.findById(eq(2L))).thenReturn(Optional.of(customer));
            when(loanRepository.findAllByOwnedBy_IdAndIsPaid(eq(2L), eq(false))).thenReturn(loans);
            when(loanMapper.toLoanDTOList(loans)).thenReturn(List.of(new LoanDTO(1L, new BigDecimal("1000"), false)));

            List<LoanDTO> result = loanService.getLoansByCustomer(2L, false);

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(1L, result.get(0).getId());
        }
    }
}
