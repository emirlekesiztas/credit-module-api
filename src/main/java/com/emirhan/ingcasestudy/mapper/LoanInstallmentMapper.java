package com.emirhan.ingcasestudy.mapper;


import com.emirhan.ingcasestudy.dto.LoanInstallmentDTO;
import com.emirhan.ingcasestudy.entity.LoanInstallment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LoanInstallmentMapper {

    List<LoanInstallmentDTO> toLoanInstallmentDTOList(List<LoanInstallment> loanInstallments);
}
