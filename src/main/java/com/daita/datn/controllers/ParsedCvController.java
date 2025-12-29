package com.daita.datn.controllers;

import com.daita.datn.common.base.ApiResponse;
import com.daita.datn.common.constants.MessageConstant;
import com.daita.datn.models.dto.ParsedCvDTO;
import com.daita.datn.services.JobSeekerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/parsed-cvs")
@RequiredArgsConstructor
public class ParsedCvController {

    private final JobSeekerService jobSeekerService;

    @GetMapping("/{cvId}")
    @PreAuthorize("hasAnyRole('JOB_SEEKER','RECRUITER','ADMIN')")
    public ApiResponse<ParsedCvDTO> getParsedCv(
            @PathVariable String cvId
    ) {
        ParsedCvDTO dto = jobSeekerService.getParsedCvById(cvId);
        return ApiResponse.<ParsedCvDTO>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.PARSED_CV_FETCH_SUCCESS)
                .data(dto)
                .build();
    }
}
