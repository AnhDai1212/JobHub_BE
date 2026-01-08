package com.daita.datn.controllers;

import com.daita.datn.common.base.ApiResponse;
import com.daita.datn.common.constants.MessageConstant;
import com.daita.datn.models.dto.ApplicationDTO;
import com.daita.datn.models.dto.CandidateChatRequest;
import com.daita.datn.models.dto.CandidateChatResponse;
import com.daita.datn.models.dto.BaseSearchDTO;
import com.daita.datn.models.dto.JobCreateRequest;
import com.daita.datn.models.dto.JobDTO;
import com.daita.datn.models.dto.JobFilterDTO;
import com.daita.datn.models.dto.JobApplicationsCountRequest;
import com.daita.datn.models.dto.JobStatusUpdateRequest;
import com.daita.datn.models.dto.JobUpdateRequest;
import com.daita.datn.models.dto.pagination.PageListDTO;
import com.daita.datn.services.JobService;
import com.daita.datn.services.CandidateChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;
    private final CandidateChatService candidateChatService;

    @PostMapping
    @PreAuthorize("hasRole('RECRUITER')")
    public ApiResponse<JobDTO> createJob(@RequestBody @Valid JobCreateRequest request) {
        JobDTO job = jobService.createJob(request);
        return ApiResponse.<JobDTO>builder()
                .code(HttpStatus.CREATED.value())
                .status(HttpStatus.CREATED.getReasonPhrase())
                .message(MessageConstant.JOB_CREATE_SUCCESS)
                .data(job)
                .build();
    }

    @GetMapping("/{jobId}")
    public ApiResponse<JobDTO> getJobDetail(@PathVariable Integer jobId) {
        JobDTO job = jobService.getPublicJobById(jobId);
        return ApiResponse.<JobDTO>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.JOB_FETCH_SUCCESS)
                .data(job)
                .build();
    }

    @GetMapping("/{jobId}/owner")
    @PreAuthorize("hasRole('RECRUITER')")
    public ApiResponse<JobDTO> getJobDetailForRecruiter(@PathVariable Integer jobId) {
        JobDTO job = jobService.getJobForCurrentRecruiter(jobId);
        return ApiResponse.<JobDTO>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.JOB_FETCH_SUCCESS)
                .data(job)
                .build();
    }

    @GetMapping
    @PreAuthorize("hasRole('RECRUITER')")
    public ApiResponse<PageListDTO<JobDTO>> getMyJobs(
            @RequestBody BaseSearchDTO<JobFilterDTO> request
    ) {
        PageListDTO<JobDTO> jobs = jobService.getJobsForCurrentRecruiter(request);
        return ApiResponse.<PageListDTO<JobDTO>>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.JOB_LIST_SUCCESS)
                .data(jobs)
                .build();
    }

    @PostMapping("/search")
    public ApiResponse<PageListDTO<JobDTO>> searchJobs(
            @RequestBody BaseSearchDTO<JobFilterDTO> request
    ) {
        PageListDTO<JobDTO> jobs = jobService.searchPublicJobs(request);
        return ApiResponse.<PageListDTO<JobDTO>>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.JOB_LIST_SUCCESS)
                .data(jobs)
                .build();
    }

    @PostMapping("/recommended")
    public ApiResponse<PageListDTO<JobDTO>> recommendJobs(
            @RequestBody BaseSearchDTO<Void> request
    ) {
        PageListDTO<JobDTO> jobs = jobService.recommendJobs(request);
        return ApiResponse.<PageListDTO<JobDTO>>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.JOB_LIST_SUCCESS)
                .data(jobs)
                .build();
    }

    @PatchMapping("/{jobId}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ApiResponse<JobDTO> updateJob(
            @PathVariable Integer jobId,
            @RequestBody @Valid JobUpdateRequest request
    ) {
        JobDTO job = jobService.updateJob(jobId, request);
        return ApiResponse.<JobDTO>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.JOB_UPDATE_SUCCESS)
                .data(job)
                .build();
    }

    @PatchMapping("/{jobId}/status")
    @PreAuthorize("hasRole('RECRUITER')")
    public ApiResponse<JobDTO> updateJobStatus(
            @PathVariable Integer jobId,
            @RequestBody @Valid JobStatusUpdateRequest request
    ) {
        JobDTO job = jobService.updateJobStatus(jobId, request);
        return ApiResponse.<JobDTO>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.JOB_STATUS_UPDATE_SUCCESS)
                .data(job)
                .build();
    }

    @PostMapping(path = "/{jobId}/jd", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('RECRUITER')")
    public ApiResponse<JobDTO> uploadJobJd(
            @PathVariable Integer jobId,
            @RequestPart("file") MultipartFile file
    ) {
        JobDTO job = jobService.uploadJobJd(jobId, file);
        return ApiResponse.<JobDTO>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.JOB_JD_UPLOAD_SUCCESS)
                .data(job)
                .build();
    }

    @DeleteMapping("/{jobId}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ApiResponse<Void> deleteJob(@PathVariable Integer jobId) {
        jobService.deleteJob(jobId);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.JOB_DELETE_SUCCESS)
                .build();
    }

    @GetMapping("/{jobId}/applications")
    @PreAuthorize("hasRole('RECRUITER')")
    public ApiResponse<PageListDTO<ApplicationDTO>> getApplications(
            @PathVariable Integer jobId,
            @RequestBody BaseSearchDTO<Void> request
    ) {
        PageListDTO<ApplicationDTO> applications = jobService.getApplicationsForJob(jobId, request);

        return ApiResponse.<PageListDTO<ApplicationDTO>>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.APPLICATION_LIST_SUCCESS)
                .data(applications)
                .build();
    }

    @PostMapping("/applications/count")
    @PreAuthorize("hasRole('RECRUITER')")
    public ApiResponse<Map<Integer, Long>> countApplications(
            @RequestBody JobApplicationsCountRequest request
    ) {
        Map<Integer, Long> counts = jobService.countApplicationsForRecruiter(request);
        return ApiResponse.<Map<Integer, Long>>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.APPLICATION_COUNT_SUCCESS)
                .data(counts)
                .build();
    }

    @PostMapping("/{jobId}/candidates/chat")
    @PreAuthorize("hasRole('RECRUITER')")
    public ApiResponse<CandidateChatResponse> chatMatchCandidate(
            @PathVariable Integer jobId,
            @RequestBody @Valid CandidateChatRequest request
    ) {
        CandidateChatResponse response = candidateChatService.matchCandidate(jobId, request);
        return ApiResponse.<CandidateChatResponse>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.CANDIDATE_CHAT_SUCCESS)
                .data(response)
                .build();
    }
}
