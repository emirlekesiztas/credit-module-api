package com.emirhan.ingcasestudy.repository;

import com.emirhan.ingcasestudy.entity.Loan;
import com.emirhan.ingcasestudy.entity.LoanInstallment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanInstallmentRepository extends JpaRepository<LoanInstallment, Long> {

    @Query("""
            SELECT li FROM LoanInstallment li
            WHERE (li.loan.id = :loanId) AND (li.isPaid = false)
""")
    List<LoanInstallment> findUnpaidLoanInstallments(@Param("loanId") Long loanId);



    @Query("""
        SELECT li
        FROM LoanInstallment li
        WHERE li.loan.id = :loanId
          AND li.isPaid = :isPaid
    """)
    List<LoanInstallment> findAllLoansByLoanAndFilters(@Param("loanId") Long loanId,@Param("isPaid") boolean isPaid);
}
