package com.daita.datn.controllers;

import com.daita.datn.common.base.ApiResponse;
import com.daita.datn.common.constants.MessageConstant;
import com.daita.datn.models.dto.BaseSearchDTO;
import com.daita.datn.models.dto.JobDTO;
import com.daita.datn.models.dto.JobFilterDTO;
import com.daita.datn.models.dto.pagination.PageListDTO;
import com.daita.datn.services.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recruiter/jobs")
@RequiredArgsConstructor
public class RecruiterJobController {
    private final JobService jobService;

    @PostMapping("/search")
    @PreAuthorize("hasRole('RECRUITER')")
    public ApiResponse<PageListDTO<JobDTO>> searchRecruiterJobs(
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
}
