package com.daita.datn.controllers;

import com.daita.datn.common.base.ApiResponse;
import com.daita.datn.common.constants.MessageConstant;
import com.daita.datn.models.dto.AccountStatusUpdateRequest;
import com.daita.datn.models.dto.RecruiterDTO;
import com.daita.datn.models.dto.RecruiterStatusUpdateRequest;
import com.daita.datn.models.dto.RecruiterDocumentDTO;
import com.daita.datn.services.AdminRecruiterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/recruiters")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminRecruiterController {

    private final AdminRecruiterService adminRecruiterService;

    @GetMapping("/pending")
    public ApiResponse<List<RecruiterDTO>> getPendingRecruiters() {
        List<RecruiterDTO> list = adminRecruiterService.getPendingRecruiters();
        return ApiResponse.<List<RecruiterDTO>>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.RECRUITER_PENDING_LIST_SUCCESS)
                .data(list)
                .build();
    }

    @PatchMapping("/{recruiterId}/status")
    public ApiResponse<RecruiterDTO> updateStatus(
            @PathVariable Integer recruiterId,
            @RequestBody RecruiterStatusUpdateRequest request
    ) {
        RecruiterDTO dto = adminRecruiterService.updateStatus(recruiterId, request);
        return ApiResponse.<RecruiterDTO>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.RECRUITER_STATUS_UPDATE_SUCCESS)
                .data(dto)
                .build();
    }

    @GetMapping("/{recruiterId}/documents")
    public ApiResponse<List<RecruiterDocumentDTO>> getRecruiterDocuments(
            @PathVariable Integer recruiterId
    ) {
        List<RecruiterDocumentDTO> docs = adminRecruiterService.getDocuments(recruiterId);
        return ApiResponse.<List<RecruiterDocumentDTO>>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.RECRUITER_DOCUMENT_LIST_SUCCESS)
                .data(docs)
                .build();
    }

    @PatchMapping("/{recruiterId}/account-status")
    public ApiResponse<RecruiterDTO> updateAccountStatus(
            @PathVariable Integer recruiterId,
            @RequestBody AccountStatusUpdateRequest request
    ) {
        RecruiterDTO dto = adminRecruiterService.updateAccountStatus(recruiterId, request);
        return ApiResponse.<RecruiterDTO>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.RECRUITER_ACCOUNT_STATUS_UPDATE_SUCCESS)
                .data(dto)
                .build();
    }
}
