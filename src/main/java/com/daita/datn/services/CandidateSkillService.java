package com.daita.datn.services;

import com.daita.datn.models.dto.SkillDTO;

import java.util.List;

public interface CandidateSkillService {
    List<SkillDTO> listMySkills();

    SkillDTO createSkill(SkillDTO request);

    SkillDTO updateSkill(Integer skillId, SkillDTO request);

    void deleteSkill(Integer skillId);
}
