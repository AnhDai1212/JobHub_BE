package com.daita.datn.controllers;

import com.daita.datn.common.base.ApiResponse;
import com.daita.datn.common.constants.MessageConstant;
import com.daita.datn.models.dto.RecruiterProfileResponse;
import com.daita.datn.models.dto.UpgradeRecruiterDTO;
import com.daita.datn.models.dto.RecruiterRegisterResponse;
import com.daita.datn.models.dto.RecruiterConsultationRequest;
import com.daita.datn.models.dto.RecruiterConsultationResponse;
import com.daita.datn.models.dto.BaseSearchDTO;
import com.daita.datn.models.dto.RecruiterDTO;
import com.daita.datn.models.dto.pagination.PageListDTO;
import com.daita.datn.services.RecruiterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/recruiters")
@RequiredArgsConstructor
public class RecruiterController {

    private final RecruiterService recruiterService;

    @PostMapping("/upgrade")
    public RecruiterRegisterResponse upgrade(
            @RequestBody @Valid UpgradeRecruiterDTO dto
    ) {
        return recruiterService.upgradeToRecruiter(dto);
    }

    @GetMapping("/me")
    public ApiResponse<RecruiterProfileResponse> getCurrentRecruiter() {
        RecruiterProfileResponse response = recruiterService.getCurrentRecruiter();

        return ApiResponse.<RecruiterProfileResponse>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.RECRUITER_FETCH_SUCCESS)
                .data(response)
                .build();
    }

    @PostMapping("/consultations")
    public ApiResponse<RecruiterConsultationResponse> saveConsultation(
            @RequestBody @Valid RecruiterConsultationRequest request
    ) {
        RecruiterConsultationResponse response = recruiterService.saveConsultation(request);
        return ApiResponse.<RecruiterConsultationResponse>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.CONSULTATION_SAVE_SUCCESS)
                .data(response)
                .build();
    }

    @PatchMapping(
            value = "/me/avatar",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('RECRUITER')")
    public ApiResponse<RecruiterProfileResponse> updateAvatar(
            @RequestPart("avatar") MultipartFile avatar
    ) throws IOException {
        RecruiterProfileResponse response = recruiterService.updateAvatar(avatar);
        return ApiResponse.<RecruiterProfileResponse>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.RECRUITER_AVATAR_UPDATE_SUCCESS)
                .data(response)
                .build();
    }

    @PostMapping("/search")
    public ApiResponse<PageListDTO<RecruiterDTO>> searchRecruiters(
            @RequestBody BaseSearchDTO<RecruiterDTO> request
    ) {
        PageListDTO<RecruiterDTO> list = recruiterService.searchRecruiters(request);
        return ApiResponse.<PageListDTO<RecruiterDTO>>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.RECRUITER_LIST_SUCCESS)
                .data(list)
                .build();
    }

}
