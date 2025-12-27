package com.daita.datn.repositories;

import com.daita.datn.models.entities.CandidateSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CandidateSkillRepository extends JpaRepository<CandidateSkill, Integer> {
    List<CandidateSkill> findByJobSeeker_JobSeekerId(Integer jobSeekerId);
}
