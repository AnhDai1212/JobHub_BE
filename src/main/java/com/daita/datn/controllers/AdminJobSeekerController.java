package com.daita.datn.controllers;

import com.daita.datn.common.base.ApiResponse;
import com.daita.datn.common.constants.MessageConstant;
import com.daita.datn.models.dto.AccountStatusUpdateRequest;
import com.daita.datn.models.dto.JobSeekerDTO;
import com.daita.datn.services.AdminJobSeekerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/job-seekers")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminJobSeekerController {

    private final AdminJobSeekerService adminJobSeekerService;

    @PatchMapping("/{jobSeekerId}/account-status")
    public ApiResponse<JobSeekerDTO> updateAccountStatus(
            @PathVariable Integer jobSeekerId,
            @RequestBody AccountStatusUpdateRequest request
    ) {
        JobSeekerDTO dto = adminJobSeekerService.updateAccountStatus(jobSeekerId, request);
        return ApiResponse.<JobSeekerDTO>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.JOB_SEEKER_ACCOUNT_STATUS_UPDATE_SUCCESS)
                .data(dto)
                .build();
    }
}
