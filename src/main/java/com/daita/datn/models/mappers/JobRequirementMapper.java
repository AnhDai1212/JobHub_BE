package com.daita.datn.models.mappers;

import com.daita.datn.models.entities.Job;
import com.daita.datn.models.entities.JobRequirement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface JobRequirementMapper {

    @Mapping(target = "requirementId", ignore = true)
    @Mapping(target = "job", source = "job")
    @Mapping(target = "requirementText", source = "requirementText")
    @Mapping(target = "displayOrder", source = "displayOrder")
    JobRequirement toEntity(String requirementText, Integer displayOrder, Job job);
}
