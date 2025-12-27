package com.daita.datn.controllers;

import com.daita.datn.common.base.ApiResponse;
import com.daita.datn.common.constants.MessageConstant;
import com.daita.datn.models.dto.ApplicationCreateRequest;
import com.daita.datn.models.dto.ApplicationDTO;
import com.daita.datn.models.dto.ApplicationDetailDTO;
import com.daita.datn.models.dto.ApplicationStatusUpdateRequest;
import com.daita.datn.models.dto.BaseSearchDTO;
import com.daita.datn.models.dto.pagination.PageListDTO;
import com.daita.datn.services.ApplicationService;
import com.daita.datn.services.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final JobService jobService;
    private final ApplicationService applicationService;

    @PostMapping
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ApiResponse<ApplicationDTO> applyJob(
            @RequestBody @Valid ApplicationCreateRequest request
    ) {
        ApplicationDTO dto = applicationService.applyJob(request);
        return ApiResponse.<ApplicationDTO>builder()
                .code(HttpStatus.CREATED.value())
                .status(HttpStatus.CREATED.getReasonPhrase())
                .message(MessageConstant.APPLICATION_APPLY_SUCCESS)
                .data(dto)
                .build();
    }

    @PatchMapping("/{applicationId}/withdraw")
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ApiResponse<ApplicationDTO> withdrawApplication(
            @PathVariable String applicationId
    ) {
        ApplicationDTO dto = applicationService.withdrawApplication(applicationId);
        return ApiResponse.<ApplicationDTO>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.APPLICATION_WITHDRAW_SUCCESS)
                .data(dto)
                .build();
    }

    @PostMapping("/search")
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ApiResponse<PageListDTO<ApplicationDTO>> listMyApplications(
            @RequestBody BaseSearchDTO<ApplicationDTO> request
    ) {
        PageListDTO<ApplicationDTO> list = applicationService.listMyApplications(request);
        return ApiResponse.<PageListDTO<ApplicationDTO>>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.APPLICATION_LIST_SUCCESS)
                .data(list)
                .build();
    }

    @GetMapping("/{applicationId}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ApiResponse<ApplicationDetailDTO> getApplicationDetail(
            @PathVariable String applicationId
    ) {
        ApplicationDetailDTO dto = jobService.getApplicationDetail(applicationId);
        return ApiResponse.<ApplicationDetailDTO>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.APPLICATION_FETCH_SUCCESS)
                .data(dto)
                .build();
    }

    @PatchMapping("/{applicationId}/status")
    @PreAuthorize("hasRole('RECRUITER')")
    public ApiResponse<ApplicationDTO> updateApplicationStatus(
            @PathVariable String applicationId,
            @RequestBody @Valid ApplicationStatusUpdateRequest request
    ) {
        ApplicationDTO dto = jobService.updateApplicationStatus(applicationId, request);
        return ApiResponse.<ApplicationDTO>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.APPLICATION_STATUS_UPDATE_SUCCESS)
                .data(dto)
                .build();
    }
}
