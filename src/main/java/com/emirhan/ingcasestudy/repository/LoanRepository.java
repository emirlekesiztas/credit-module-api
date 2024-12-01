package com.emirhan.ingcasestudy.repository;

import com.emirhan.ingcasestudy.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findAllByOwnedBy_IdAndIsPaid(Long customerId, boolean isPaid);
}
