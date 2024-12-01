package com.emirhan.ingcasestudy.repository;

import com.emirhan.ingcasestudy.entity.Loan;
import com.emirhan.ingcasestudy.entity.LoanInstallment;
import com.emirhan.ingcasestudy.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class LoanInstallmentRepositoryTest {

    @Autowired
    private LoanInstallmentRepository loanInstallmentRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Test
    void testFindUnpaidLoanInstallments_Success() {
        User customer = new User();
        customer.setEmail("test123@gmail.com");
        customer.setFirstName("Emirhan");
        customer.setLastName("Lekesiztaş");
        customer.setPassword("test1234");
        customer.setCreditLimit(new BigDecimal("500000"));
        userRepository.saveAndFlush(customer);

        Loan loan = new Loan();
        loan.setOwnedBy(customer);
        loan.setNumberOfInstallments(6);
        loan.setIsPaid(false);
        loan.setLoanAmount(new BigDecimal("1000"));
        loanRepository.saveAndFlush(loan);

        loan = loanRepository.findById(loan.getId()).orElseThrow(() -> new IllegalStateException("Couldn't find the loan."));

        LoanInstallment installment = new LoanInstallment();
        installment.setLoan(loan);
        installment.setAmount(BigDecimal.valueOf(500));
        installment.setIsPaid(false);
        installment.setDueDate(LocalDate.now().plusDays(30));
        loanInstallmentRepository.saveAndFlush(installment);

        List<LoanInstallment> unpaidInstallments = loanInstallmentRepository.findUnpaidLoanInstallments(loan.getId());

        assertEquals(1, unpaidInstallments.size());
        assertEquals(installment.getAmount(), unpaidInstallments.get(0).getAmount());
        assertEquals(loan.getId(), unpaidInstallments.get(0).getLoan().getId());
    }


}
