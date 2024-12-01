package com.emirhan.ingcasestudy.service;

import com.emirhan.ingcasestudy.dto.LoanInstallmentDTO;
import com.emirhan.ingcasestudy.entity.Loan;
import com.emirhan.ingcasestudy.entity.LoanInstallment;
import com.emirhan.ingcasestudy.entity.UserRole;
import com.emirhan.ingcasestudy.mapper.LoanInstallmentMapper;
import com.emirhan.ingcasestudy.repository.LoanInstallmentRepository;
import com.emirhan.ingcasestudy.repository.LoanRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.emirhan.ingcasestudy.util.AuthUtil.getCurrentUserId;
import static com.emirhan.ingcasestudy.util.AuthUtil.getCurrentUserRole;

@Service
@AllArgsConstructor
public class LoanInstallmentService {
    private final LoanInstallmentRepository loanInstallmentRepository;
    private final LoanInstallmentMapper loanInstallmentMapper;
    private final LoanRepository loanRepository;

    public List<LoanInstallmentDTO> getLoanInstallmentsByLoan(Long loanId, Boolean isPaid) {
        Loan loan = loanRepository.findById(loanId).orElseThrow(() -> new IllegalStateException("Couldn't find the Loan."));
        if (getCurrentUserRole().equals(UserRole.CUSTOMER.toString()) && !loan.getOwnedBy().getId().equals(getCurrentUserId())) {
            throw new IllegalArgumentException("Customers can only list their own loan installments");
        }
        List<LoanInstallment> loanInstallments = loanInstallmentRepository.findAllLoansByLoanAndFilters(loanId, isPaid);
        return loanInstallmentMapper.toLoanInstallmentDTOList(loanInstallments);
    }
}
