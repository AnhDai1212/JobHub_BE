package com.daita.datn.models.mappers;

import com.daita.datn.models.dto.RecruiterProfileResponse;
import com.daita.datn.models.dto.RecruiterRegisterResponse;
import com.daita.datn.models.dto.RecruiterConsultationResponse;
import com.daita.datn.models.dto.RecruiterConsultationRequest;
import com.daita.datn.models.dto.RecruiterDTO;
import com.daita.datn.models.dto.UpgradeRecruiterDTO;
import com.daita.datn.models.entities.Company;
import com.daita.datn.models.entities.RecruiterConsultation;
import com.daita.datn.models.entities.Recruiter;
import com.daita.datn.models.entities.auth.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RecruiterMapper {

    @Mapping(source = "company.companyId", target = "companyId")
    @Mapping(source = "company.companyName", target = "companyName")
    RecruiterRegisterResponse toRegisterResponse(Recruiter recruiter);

    @Mapping(source = "company.companyId", target = "companyId")
    @Mapping(source = "company.companyName", target = "companyName")
    @Mapping(source = "avatarUrl", target = "avatarUrl")
    RecruiterProfileResponse toProfileResponse(Recruiter recruiter);

    RecruiterConsultationResponse toConsultationResponse(RecruiterConsultation consultation);

    @Mapping(source = "account.accountId", target = "accountId")
    @Mapping(source = "account.email", target = "email")
    @Mapping(source = "account.status", target = "accountStatus")
    @Mapping(source = "company.companyId", target = "companyId")
    @Mapping(source = "company.companyName", target = "companyName")
    RecruiterDTO toDto(Recruiter recruiter);

    @Mapping(target = "recruiter", source = "recruiter")
    RecruiterConsultation toConsultation(RecruiterConsultationRequest request, Recruiter recruiter);

    @Mapping(target = "companyName", source = "companyName")
    @Mapping(target = "location", source = "dto.location")
    @Mapping(target = "website", source = "dto.website")
    @Mapping(target = "introduction", source = "dto.introduction")
    @Mapping(target = "isApproved", constant = "false")
    Company toCompany(UpgradeRecruiterDTO dto, String companyName);

    @Mapping(target = "account", source = "account")
    @Mapping(target = "company", source = "company")
    @Mapping(target = "position", source = "dto.position")
    @Mapping(target = "phone", source = "dto.phone")
    @Mapping(target = "status", constant = "PENDING")
    Recruiter toRecruiter(UpgradeRecruiterDTO dto, Account account, Company company);

    void updateConsultation(RecruiterConsultationRequest request, @MappingTarget RecruiterConsultation consultation);
}
