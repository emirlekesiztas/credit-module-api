package com.emirhan.ingcasestudy.mapper;

import com.emirhan.ingcasestudy.dto.LoanDTO;
import com.emirhan.ingcasestudy.entity.Loan;
import com.emirhan.ingcasestudy.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LoanMapper {


    @Mapping(source = "ownedBy.email", target = "customerEmail")
    @Mapping(source = "ownedBy.id", target = "customerId")
    @Mapping(source = "ownedBy", target = "customerFullName", qualifiedByName = "fullNameMapper")
    LoanDTO toLoanDTO(Loan loan);

    List<LoanDTO> toLoanDTOList(List<Loan> loans);

    @Named("fullNameMapper")
    default String mapFullName(User user) {
        if (user == null) {
            return null;
        }
        return user.getFirstName() + " " + user.getLastName();
    }
}
