package com.emirhan.ingcasestudy.repository;

import com.emirhan.ingcasestudy.entity.InstallmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstallmentTypeRepository extends JpaRepository<InstallmentType, Long> {
    Boolean existsByInstallmentNr(int installmentNr);
}
