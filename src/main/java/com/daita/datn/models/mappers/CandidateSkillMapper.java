package com.daita.datn.models.mappers;

import com.daita.datn.models.dto.SkillDTO;
import com.daita.datn.models.entities.CandidateSkill;
import com.daita.datn.models.entities.JobSeeker;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CandidateSkillMapper {

    SkillDTO toDto(CandidateSkill skill);

    List<SkillDTO> toDtoList(List<CandidateSkill> skills);

    @Mapping(target = "skillId", ignore = true)
    @Mapping(target = "jobSeeker", source = "jobSeeker")
    CandidateSkill toEntity(SkillDTO dto, JobSeeker jobSeeker);

    @Mapping(target = "skillId", ignore = true)
    @Mapping(target = "jobSeeker", ignore = true)
    void updateFromDto(SkillDTO dto, @MappingTarget CandidateSkill skill);
}
