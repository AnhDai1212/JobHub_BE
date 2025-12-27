package com.daita.datn.models.mappers;

import com.daita.datn.common.constants.Constant;
import com.daita.datn.models.dto.company.CompanyRequestDTO;
import com.daita.datn.models.dto.company.CompanyResponseDTO;
import com.daita.datn.models.dto.company.CompanyUpdateDTO;
import com.daita.datn.models.entities.Company;
import com.daita.datn.models.dto.CompanySuggestionDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;


@Mapper(componentModel = "spring", imports = Constant.class)
public interface CompanyMapper {

    @Mapping(source = "createAt", target = "createdAt")
    CompanyResponseDTO toDTO(Company company);

    Company toEntity(CompanyRequestDTO companyDTO);

    @Mapping(target = "isApproved", constant = "true")
    @Mapping(
            target = "avatarUrl",
            expression = "java(Constant.DEFAULT_COMPANY_AVATAR)"
    )
    Company toEntityWithDefaults(CompanyRequestDTO companyDTO);

    void updateEntity(CompanyUpdateDTO dto, @MappingTarget Company company);

    CompanySuggestionDTO toSuggestion(Company company);
}

