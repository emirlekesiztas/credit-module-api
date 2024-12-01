package com.emirhan.ingcasestudy.repository;

import com.emirhan.ingcasestudy.entity.InstallmentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class InstallmentTypeRepositoryTest {

    @Autowired
    private InstallmentTypeRepository installmentTypeRepository;

    @Test
    void testExistsByInstallmentNr_Success() {
        InstallmentType type = new InstallmentType();
        type.setId(2L);
        type.setInstallmentNr(12);
        installmentTypeRepository.save(type);

        boolean exists = installmentTypeRepository.existsByInstallmentNr(12);

        assertTrue(exists);
    }
}

