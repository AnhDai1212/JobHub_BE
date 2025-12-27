package com.daita.datn.models.mappers;

import com.daita.datn.models.dto.JobSeekerCreateRequest;
import com.daita.datn.models.dto.JobSeekerDTO;
import com.daita.datn.models.dto.JobSeekerUpdateRequest;
import com.daita.datn.models.entities.JobSeeker;
import com.daita.datn.models.entities.auth.Account;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface JobSeekerMapper {

    @Mapping(source = "account.accountId", target = "accountId")
    @Mapping(source = "account.email", target = "email")
    @Mapping(source = "account.status", target = "accountStatus")
    @Mapping(source = "createAt", target = "createdAt")
    @Mapping(source = "avatarUrl", target = "avatarUrl")
    JobSeekerDTO toDto(JobSeeker jobSeeker);

    @Mapping(source = "account", target = "account")
    JobSeeker toEntity(JobSeekerCreateRequest request, Account account);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "jobSeekerId", ignore = true)
    @Mapping(target = "account", ignore = true)
    @Mapping(target = "cvUrl", ignore = true)
    @Mapping(target = "avatarUrl", ignore = true)
    @Mapping(target = "applications", ignore = true)
    @Mapping(target = "favorites", ignore = true)
    @Mapping(target = "skills", ignore = true)
    @Mapping(target = "jobRecommendations", ignore = true)
    @Mapping(target = "parsedCvs", ignore = true)
    @Mapping(target = "createAt", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    void updateFromRequest(JobSeekerUpdateRequest request, @MappingTarget JobSeeker jobSeeker);
}
