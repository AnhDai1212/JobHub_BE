package com.daita.datn.services.implement;

import com.daita.datn.enums.ErrorCode;
import com.daita.datn.exceptions.AppException;
import com.daita.datn.models.dto.SkillDTO;
import com.daita.datn.models.entities.CandidateSkill;
import com.daita.datn.models.entities.JobSeeker;
import com.daita.datn.models.entities.auth.Account;
import com.daita.datn.models.mappers.CandidateSkillMapper;
import com.daita.datn.repositories.CandidateSkillRepository;
import com.daita.datn.repositories.JobSeekerRepository;
import com.daita.datn.services.AccountService;
import com.daita.datn.services.CandidateSkillService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CandidateSkillServiceImpl implements CandidateSkillService {

    CandidateSkillRepository candidateSkillRepository;
    JobSeekerRepository jobSeekerRepository;
    AccountService accountService;
    CandidateSkillMapper candidateSkillMapper;

    @Override
    @Transactional(readOnly = true)
    public List<SkillDTO> listMySkills() {
        JobSeeker jobSeeker = getCurrentJobSeeker();
        List<CandidateSkill> skills = candidateSkillRepository
                .findByJobSeeker_JobSeekerId(jobSeeker.getJobSeekerId());
        return candidateSkillMapper.toDtoList(skills);
    }

    @Override
    @Transactional
    public SkillDTO createSkill(SkillDTO request) {
        JobSeeker jobSeeker = getCurrentJobSeeker();
        CandidateSkill skill = candidateSkillMapper.toEntity(request, jobSeeker);
        CandidateSkill saved = candidateSkillRepository.save(skill);
        return candidateSkillMapper.toDto(saved);
    }

    @Override
    @Transactional
    public SkillDTO updateSkill(Integer skillId, SkillDTO request) {
        JobSeeker jobSeeker = getCurrentJobSeeker();
        CandidateSkill skill = candidateSkillRepository.findById(skillId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Skill"));

        if (!Objects.equals(skill.getJobSeeker().getJobSeekerId(), jobSeeker.getJobSeekerId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        candidateSkillMapper.updateFromDto(request, skill);
        CandidateSkill saved = candidateSkillRepository.save(skill);
        return candidateSkillMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void deleteSkill(Integer skillId) {
        JobSeeker jobSeeker = getCurrentJobSeeker();
        CandidateSkill skill = candidateSkillRepository.findById(skillId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Skill"));

        if (!Objects.equals(skill.getJobSeeker().getJobSeekerId(), jobSeeker.getJobSeekerId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        candidateSkillRepository.delete(skill);
    }

    private JobSeeker getCurrentJobSeeker() {
        Account account = accountService.getCurrentAccount();
        return jobSeekerRepository.findByAccount_AccountId(account.getAccountId())
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "JobSeeker"));
    }
}
