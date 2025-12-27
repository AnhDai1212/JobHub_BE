package com.daita.datn.controllers;

import com.daita.datn.common.base.ApiResponse;
import com.daita.datn.common.constants.MessageConstant;
import com.daita.datn.models.dto.SkillDTO;
import com.daita.datn.services.CandidateSkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/job-seekers/skills")
@RequiredArgsConstructor
public class CandidateSkillController {

    private final CandidateSkillService candidateSkillService;

    @GetMapping
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ApiResponse<List<SkillDTO>> listMySkills() {
        List<SkillDTO> list = candidateSkillService.listMySkills();
        return ApiResponse.<List<SkillDTO>>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.SKILL_LIST_SUCCESS)
                .data(list)
                .build();
    }

    @PostMapping
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ApiResponse<SkillDTO> createSkill(
            @RequestBody SkillDTO request
    ) {
        SkillDTO dto = candidateSkillService.createSkill(request);
        return ApiResponse.<SkillDTO>builder()
                .code(HttpStatus.CREATED.value())
                .status(HttpStatus.CREATED.getReasonPhrase())
                .message(MessageConstant.SKILL_CREATE_SUCCESS)
                .data(dto)
                .build();
    }

    @PatchMapping("/{skillId}")
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ApiResponse<SkillDTO> updateSkill(
            @PathVariable Integer skillId,
            @RequestBody SkillDTO request
    ) {
        SkillDTO dto = candidateSkillService.updateSkill(skillId, request);
        return ApiResponse.<SkillDTO>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.SKILL_UPDATE_SUCCESS)
                .data(dto)
                .build();
    }

    @DeleteMapping("/{skillId}")
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ApiResponse<Void> deleteSkill(
            @PathVariable Integer skillId
    ) {
        candidateSkillService.deleteSkill(skillId);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.SKILL_DELETE_SUCCESS)
                .build();
    }
}
