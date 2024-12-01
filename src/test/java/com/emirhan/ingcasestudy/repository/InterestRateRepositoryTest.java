package com.emirhan.ingcasestudy.repository;

import com.emirhan.ingcasestudy.entity.InterestRate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class InterestRateRepositoryTest {

    @Autowired
    private InterestRateRepository interestRateRepository;

    @Test
    void testFindByAddedOnYear_Success() {
        InterestRate rate = new InterestRate();
        rate.setId(1L);
        rate.setAddedOn(new Date());
        rate.setInterestRate(BigDecimal.valueOf(0.05));
        interestRateRepository.save(rate);

        Optional<InterestRate> foundRate = interestRateRepository.findByAddedOnYear(2024);

        assertTrue(foundRate.isPresent());
        assertEquals(BigDecimal.valueOf(0.05), foundRate.get().getInterestRate());
    }

}
