package com.emirhan.ingcasestudy.repository;

import com.emirhan.ingcasestudy.entity.InterestRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InterestRateRepository extends JpaRepository<InterestRate, Long> {
    @Query("SELECT i FROM InterestRate i WHERE FUNCTION('YEAR', i.addedOn) = :year")
    Optional<InterestRate> findByAddedOnYear(@Param("year") int year);
}
