package com.daita.datn.models.mappers;

import com.daita.datn.models.dto.JobDTO;
import com.daita.datn.models.entities.Job;
import com.daita.datn.models.dto.JobUpdateRequest;
import com.daita.datn.models.dto.JobCreateRequest;
import com.daita.datn.models.entities.JobCategory;
import com.daita.datn.models.entities.JobTag;
import com.daita.datn.models.entities.Company;
import com.daita.datn.models.entities.Recruiter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.BeanMapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface JobMapper {

    @Mapping(source = "company.companyId", target = "companyId")
    @Mapping(source = "recruiter.recruiterId", target = "recruiterId")
    @Mapping(source = "createAt", target = "createdAt")
    @Mapping(source = "company.avatarUrl", target = "companyAvatarUrl")
    @Mapping(target = "categories", expression = "java(mapCategoryNames(job.getCategories()))")
    @Mapping(target = "tags", expression = "java(mapTagNames(job.getTags()))")
    JobDTO toDto(Job job);

    List<JobDTO> toDtoList(List<Job> jobs);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateJobFromRequest(JobUpdateRequest request, @MappingTarget Job job);

    @Mapping(target = "company", source = "company")
    @Mapping(target = "recruiter", source = "recruiter")
    @Mapping(target = "status", constant = "OPEN")
    @Mapping(target = "location", source = "request.location")
    @Mapping(target = "jobId", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "applications", ignore = true)
    @Mapping(target = "favorites", ignore = true)
    @Mapping(target = "recommendations", ignore = true)
    Job toEntity(JobCreateRequest request, Company company, Recruiter recruiter);

    default Set<String> mapCategoryNames(Set<JobCategory> categories) {
        if (categories == null) return null;
        return categories.stream()
                .map(JobCategory::getCategoryName)
                .collect(Collectors.toSet());
    }

    default Set<String> mapTagNames(Set<JobTag> tags) {
        if (tags == null) return null;
        return tags.stream()
                .map(JobTag::getTagName)
                .collect(Collectors.toSet());
    }
}
