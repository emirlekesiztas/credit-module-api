package com.emirhan.ingcasestudy.service;

import com.emirhan.ingcasestudy.dto.CreateLoanRequest;
import com.emirhan.ingcasestudy.dto.LoanDTO;
import com.emirhan.ingcasestudy.dto.LoanPayRequest;
import com.emirhan.ingcasestudy.dto.LoanPayResponse;
import com.emirhan.ingcasestudy.entity.Loan;
import com.emirhan.ingcasestudy.entity.LoanInstallment;
import com.emirhan.ingcasestudy.entity.User;
import com.emirhan.ingcasestudy.entity.UserRole;
import com.emirhan.ingcasestudy.mapper.LoanMapper;
import com.emirhan.ingcasestudy.repository.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Year;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.emirhan.ingcasestudy.util.AuthUtil.getCurrentUserId;
import static com.emirhan.ingcasestudy.util.AuthUtil.getCurrentUserRole;

@Service
@RequiredArgsConstructor
public class LoanService {

    private static final Logger logger = LoggerFactory.getLogger(LoanService.class);

    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final InterestRateRepository interestRateRepository;
    private final InstallmentTypeRepository installmentTypeRepository;
    private final LoanInstallmentRepository loanInstallmentRepository;
    private final LoanMapper loanMapper;

    public LoanPayResponse payLoan(LoanPayRequest request) {
        BigDecimal totalPaidAmount = request.getPaidAmount();
        BigDecimal refundAmount = BigDecimal.ZERO;
        LocalDate paidDate = LocalDate.now();
        int totalInstallmentPaid = 0;

        Loan loan = loanRepository.findById(request.getLoanId()).orElseThrow(() -> new IllegalArgumentException("Couldn't find loan."));
        validateUserForPayLoan(loan);

        List<LoanInstallment> installments = loanInstallmentRepository.findUnpaidLoanInstallments(request.getLoanId()).stream()
                .sorted(Comparator.comparing(LoanInstallment::getInstallmentNr))
                .collect(Collectors.toList());

        for (LoanInstallment installment : installments) {
            long dayDiff = ChronoUnit.DAYS.between(paidDate,installment.getDueDate());
            long monthDiff = ChronoUnit.MONTHS.between(paidDate.withDayOfMonth(1), installment.getDueDate().withDayOfMonth(1));

            if (totalPaidAmount.compareTo(installment.getAmount()) < 0 || monthDiff > 3) {
                refundAmount = totalPaidAmount;
                break;
            }

            BigDecimal coefficientValue = BigDecimal.valueOf(Math.abs(dayDiff) * 0.001);
            if (dayDiff < 0) {
                installment.setAmount(installment.getAmount().add(coefficientValue));
            } else {
                installment.setAmount(installment.getAmount().subtract(coefficientValue));
            }

            installment.setPaymentDate(paidDate);
            installment.setPaidAmount(installment.getAmount());
            installment.setIsPaid(true);

            totalPaidAmount = totalPaidAmount.subtract(installment.getPaidAmount());
            loan.setLoanAmountWithInterestRate(loan.getLoanAmountWithInterestRate().subtract(installment.getPaidAmount()));
            totalInstallmentPaid++;
        }

        loan.setIsPaid(loan.getLoanAmountWithInterestRate().compareTo(BigDecimal.ZERO) < 1);
        loanRepository.save(loan);
        loanInstallmentRepository.saveAll(installments);

        logger.info("Installments paid for loan id: " + loan.getId());
        return prepareLoanPayResponse(loan, request, refundAmount, totalInstallmentPaid);
    }

    public LoanDTO createLoan(CreateLoanRequest request) {
        User customer = userRepository.findById(request.getCustomerId()).orElseThrow(() -> new IllegalStateException("Couldn't find user."));

        validateUserForCreateLoan(customer);

        if (request.getLoanAmount().compareTo(customer.getCreditLimit()) > 0) {
            throw new IllegalArgumentException("Users can not get Loan more than their credit limits.");
        }

        if (!isNumberOfInstallmentValid(request.getNumberOfInstallments())) {
            throw new IllegalArgumentException();
        }

        Loan loan = new Loan();

        BigDecimal interestRate = interestRateRepository.findByAddedOnYear(Year.now().getValue()).orElseThrow(() -> new IllegalStateException("Couldn't find interest rate.")).getInterestRate();
        BigDecimal loanAmountWithInterestRate = request.getLoanAmount().multiply((BigDecimal.ONE.add(interestRate)));

        loan.setLoanAmount(request.getLoanAmount());
        loan.setLoanAmountWithInterestRate(loanAmountWithInterestRate);
        loan.setCreateDate(new Date());
        loan.setNumberOfInstallments(request.getNumberOfInstallments());
        loan.setOwnedBy(customer);

        loanRepository.save(loan);
        createLoanInstallments(loan, request.getNumberOfInstallments());

        customer.setUsedCreditLimit(loanAmountWithInterestRate);
        customer.setCreditLimit(customer.getCreditLimit().subtract(loanAmountWithInterestRate));
        userRepository.save(customer);

        return loanMapper.toLoanDTO(loan);
    }

    private void validateUserForPayLoan(Loan loan) {
        if (getCurrentUserRole().equals(UserRole.CUSTOMER.toString()) && !loan.getOwnedBy().getId().equals(getCurrentUserId())) {
            throw new IllegalArgumentException("Customers can only pay for their own loans");
        }
    }

    private void validateUserForCreateLoan(User customer) {
        if (getCurrentUserRole().equals(UserRole.CUSTOMER.toString()) && !customer.getId().equals(getCurrentUserId())) {
            throw new IllegalArgumentException("Customers can only create their own loans");
        }
    }

    private void validateUserForListLoan(Long customerId) {
        if (getCurrentUserRole().equals(UserRole.CUSTOMER.toString()) && !customerId.equals(getCurrentUserId())) {
            throw new IllegalArgumentException("Customers can only list their own loans");
        }
    }

    public List<LoanDTO> getLoansByCustomer(Long customerId, boolean isPaid) {
        validateUserForListLoan(customerId);
        List<Loan> loans = loanRepository.findAllByOwnedBy_IdAndIsPaid(customerId, isPaid);
        return loanMapper.toLoanDTOList(loans);
    }


    private LoanPayResponse prepareLoanPayResponse(Loan loan, LoanPayRequest request, BigDecimal refundAmount, int totalInstallmentPaid) {
        return LoanPayResponse.builder()
                .refundAmount(refundAmount)
                .loanId(loan.getId())
                .remainingPaymentAmount(loan.getLoanAmountWithInterestRate())
                .customerId(loan.getOwnedBy().getId())
                .isLoanPaid(loan.getIsPaid())
                .totalInstallmentPaid(totalInstallmentPaid)
                .build();
    }

    private void createLoanInstallments(Loan loan, int numberOfInstallments) {
        List<LoanInstallment> installments = new ArrayList<>();

        BigDecimal installmentAmount = loan.getLoanAmountWithInterestRate()
                .divide(BigDecimal.valueOf(numberOfInstallments), 2, RoundingMode.HALF_UP);

        LocalDate installmentDate = LocalDate.now().plusMonths(1).withDayOfMonth(1);
        for (int i = 0; i < numberOfInstallments; i++) {
            LoanInstallment loanInstallment = new LoanInstallment();

            loanInstallment.setDueDate(installmentDate);
            loanInstallment.setAmount(installmentAmount);
            loanInstallment.setLoan(loan);
            loanInstallment.setIsPaid(false);

            installments.add(loanInstallment);
            installmentDate = installmentDate.plusMonths(1);
        }
        loanInstallmentRepository.saveAll(installments);
    }

    private boolean isNumberOfInstallmentValid(int installmentNr) {
        return installmentTypeRepository.existsByInstallmentNr(installmentNr);
    }
}
