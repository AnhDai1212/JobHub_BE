package com.daita.datn.controllers;

import com.daita.datn.common.base.ApiResponse;
import com.daita.datn.common.constants.MessageConstant;
import com.daita.datn.models.dto.JobSeekerDTO;
import com.daita.datn.models.dto.JobSeekerCreateRequest;
import com.daita.datn.models.dto.JobSeekerUpdateRequest;
import com.daita.datn.models.dto.CvParseResponse;
import com.daita.datn.models.dto.ParsedCvDTO;
import com.daita.datn.models.dto.ParsedCvSaveRequest;
import com.daita.datn.models.dto.BaseSearchDTO;
import com.daita.datn.models.dto.SkillDTO;
import com.daita.datn.models.dto.pagination.PageListDTO;
import com.daita.datn.services.JobSeekerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/job-seekers")
@RequiredArgsConstructor
public class JobSeekerController {

    private final JobSeekerService jobSeekerService;

    @GetMapping("/me")
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ApiResponse<JobSeekerDTO> getCurrentJobSeeker() {
        JobSeekerDTO dto = jobSeekerService.getCurrentJobSeeker();
        return ApiResponse.<JobSeekerDTO>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.JOB_SEEKER_FETCH_SUCCESS)
                .data(dto)
                .build();
    }

    @GetMapping("/{jobSeekerId}")
    public ApiResponse<JobSeekerDTO> getJobSeekerById(
            @PathVariable Integer jobSeekerId
    ) {
        JobSeekerDTO dto = jobSeekerService.getJobSeekerById(jobSeekerId);
        return ApiResponse.<JobSeekerDTO>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.JOB_SEEKER_FETCH_SUCCESS)
                .data(dto)
                .build();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<JobSeekerDTO> createProfile(
            @RequestBody @Valid JobSeekerCreateRequest request
    ) {
        JobSeekerDTO dto = jobSeekerService.createProfile(request);
        return ApiResponse.<JobSeekerDTO>builder()
                .code(HttpStatus.CREATED.value())
                .status(HttpStatus.CREATED.getReasonPhrase())
                .message(MessageConstant.JOB_SEEKER_PROFILE_CREATE_SUCCESS)
                .data(dto)
                .build();
    }

    @PatchMapping(path = "/me", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ApiResponse<JobSeekerDTO> updateProfile(
            @RequestBody @Valid JobSeekerUpdateRequest request
    ) {
        JobSeekerDTO dto = jobSeekerService.updateProfile(request);
        return ApiResponse.<JobSeekerDTO>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.JOB_SEEKER_PROFILE_UPDATE_SUCCESS)
                .data(dto)
                .build();
    }

    @PostMapping(path = "/cv/parse", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ApiResponse<CvParseResponse> parseCv(
            @RequestPart("file") MultipartFile file
    ) throws IOException {
        CvParseResponse dto = jobSeekerService.parseCv(file);
        return ApiResponse.<CvParseResponse>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.CV_PARSE_SUCCESS)
                .data(dto)
                .build();
    }

    @PostMapping(path = "/cv/online", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ApiResponse<ParsedCvDTO> saveParsedCv(
            @RequestBody @Valid ParsedCvSaveRequest request
    ) {
        ParsedCvDTO dto = jobSeekerService.saveParsedCv(request);
        return ApiResponse.<ParsedCvDTO>builder()
                .code(HttpStatus.CREATED.value())
                .status(HttpStatus.CREATED.getReasonPhrase())
                .message(MessageConstant.PARSED_CV_SAVE_SUCCESS)
                .data(dto)
                .build();
    }

    @GetMapping("/cv/online/latest")
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ApiResponse<ParsedCvDTO> getLatestParsedCv() {
        ParsedCvDTO dto = jobSeekerService.getLatestParsedCv();
        return ApiResponse.<ParsedCvDTO>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.PARSED_CV_FETCH_SUCCESS)
                .data(dto)
                .build();
    }

    @PostMapping(path = "/cv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ApiResponse<JobSeekerDTO> uploadCv(
            @RequestPart("file") MultipartFile file
    ) throws java.io.IOException {
        JobSeekerDTO dto = jobSeekerService.uploadCv(file);
        return ApiResponse.<JobSeekerDTO>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.CV_UPLOAD_SUCCESS)
                .data(dto)
                .build();
    }

    @DeleteMapping("/cv")
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ApiResponse<JobSeekerDTO> deleteCv() {
        JobSeekerDTO dto = jobSeekerService.deleteCv();
        return ApiResponse.<JobSeekerDTO>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.CV_DELETE_SUCCESS)
                .data(dto)
                .build();
    }

    @PatchMapping(
            path = "/avatar",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ApiResponse<JobSeekerDTO> updateAvatar(
            @RequestPart("avatar") MultipartFile avatar
    ) throws IOException {
        JobSeekerDTO dto = jobSeekerService.updateAvatar(avatar);
        return ApiResponse.<JobSeekerDTO>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.JOB_SEEKER_AVATAR_UPDATE_SUCCESS)
                .data(dto)
                .build();
    }

    @PostMapping("/search")
    public ApiResponse<PageListDTO<JobSeekerDTO>> searchJobSeekers(
            @RequestBody BaseSearchDTO<JobSeekerDTO> request
    ) {
        PageListDTO<JobSeekerDTO> list = jobSeekerService.searchJobSeekers(request);
        return ApiResponse.<PageListDTO<JobSeekerDTO>>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.JOB_SEEKER_LIST_SUCCESS)
                .data(list)
                .build();
    }

    @GetMapping("/skills")
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ApiResponse<List<SkillDTO>> listMySkills() {
        List<SkillDTO> list = jobSeekerService.listMySkills();
        return ApiResponse.<List<SkillDTO>>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.SKILL_LIST_SUCCESS)
                .data(list)
                .build();
    }

    @PostMapping("/skills")
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ApiResponse<SkillDTO> createSkill(
            @RequestBody SkillDTO request
    ) {
        SkillDTO dto = jobSeekerService.createSkill(request);
        return ApiResponse.<SkillDTO>builder()
                .code(HttpStatus.CREATED.value())
                .status(HttpStatus.CREATED.getReasonPhrase())
                .message(MessageConstant.SKILL_CREATE_SUCCESS)
                .data(dto)
                .build();
    }

    @PatchMapping("/skills/{skillId}")
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ApiResponse<SkillDTO> updateSkill(
            @PathVariable Integer skillId,
            @RequestBody SkillDTO request
    ) {
        SkillDTO dto = jobSeekerService.updateSkill(skillId, request);
        return ApiResponse.<SkillDTO>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.SKILL_UPDATE_SUCCESS)
                .data(dto)
                .build();
    }

    @DeleteMapping("/skills/{skillId}")
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ApiResponse<Void> deleteSkill(
            @PathVariable Integer skillId
    ) {
        jobSeekerService.deleteSkill(skillId);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.SKILL_DELETE_SUCCESS)
                .build();
    }
}
