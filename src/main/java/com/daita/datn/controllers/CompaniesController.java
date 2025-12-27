package com.daita.datn.controllers;

import com.daita.datn.common.base.ApiResponse;
import com.daita.datn.common.constants.MessageConstant;
import com.daita.datn.models.dto.CompanySuggestionDTO;
import com.daita.datn.models.dto.company.CompanyRequestDTO;
import com.daita.datn.models.dto.company.CompanyResponseDTO;
import com.daita.datn.models.dto.company.CompanyUpdateDTO;
import com.daita.datn.models.dto.JobDTO;
import com.daita.datn.models.dto.JobFilterDTO;
import com.daita.datn.models.dto.BaseSearchDTO;
import com.daita.datn.models.dto.pagination.PageListDTO;
import com.daita.datn.services.CompanyService;
import com.daita.datn.services.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompaniesController {
    private final CompanyService companyService;
    private final JobService jobService;

    @PostMapping()
    public ApiResponse<CompanyResponseDTO> createCompany(@RequestBody CompanyRequestDTO requestDTO) {
        return ApiResponse.<CompanyResponseDTO>builder()
                .code(HttpStatus.CREATED.value())
                .status(HttpStatus.CREATED.getReasonPhrase())
                .message(MessageConstant.CREATE_COMPANY_SUCCESS)
                .data( companyService.createCompany(requestDTO))
                .build();
    }

    @GetMapping("/{companyId}")
    public ApiResponse<CompanyResponseDTO> getCompanyById(@PathVariable Integer companyId) {
        return ApiResponse.<CompanyResponseDTO>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.FOUND_COMPANY_SUCCESS)
                .data(companyService.getCompanyById(companyId))
                .build();
    }

    @GetMapping("/{companyId}/jobs")
    public ApiResponse<PageListDTO<JobDTO>> getCompanyJobs(
            @PathVariable Integer companyId,
            @RequestBody BaseSearchDTO<JobFilterDTO> request
    ) {
        PageListDTO<JobDTO> jobs = jobService.getCompanyJobsPublic(companyId, request);
        return ApiResponse.<PageListDTO<JobDTO>>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.JOB_LIST_SUCCESS)
                .data(jobs)
                .build();
    }

    @PostMapping("/search")
    public ApiResponse<PageListDTO<CompanyResponseDTO>> searchCompanies(
            @RequestBody BaseSearchDTO<CompanyResponseDTO> request
    ) {
        PageListDTO<CompanyResponseDTO> list = companyService.search(request);
        return ApiResponse.<PageListDTO<CompanyResponseDTO>>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.COMPANY_LIST_SUCCESS)
                .data(list)
                .build();
    }

    @PatchMapping("/{companyId}")
    public ApiResponse<CompanyResponseDTO> updateCompanyInfo(
            @PathVariable Integer companyId,
            @RequestBody CompanyUpdateDTO requestDTO
    ) {
        CompanyResponseDTO response = companyService.updateCompanyInfo(companyId, requestDTO);

        return ApiResponse.<CompanyResponseDTO>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.COMPANY_INFO_UPDATE_SUCCESS)
                .data(response)
                .build();
    }

    @PatchMapping(
            value = "/{companyId}/avatar",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ApiResponse<CompanyResponseDTO> updateCompanyAvatar(
            @PathVariable Integer companyId,
            @RequestPart("avatar") MultipartFile avatar
    ) throws IOException {

        CompanyResponseDTO response = companyService.updateCompanyAvatar(companyId, avatar);

        return ApiResponse.<CompanyResponseDTO>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.COMPANY_AVATAR_UPDATE_SUCCESS)
                .data(response)
                .build();
    }

    @DeleteMapping("/{companyId}")
    public ApiResponse<CompanyResponseDTO> deleteCompany(@PathVariable Integer companyId) {

        companyService.deleteCompany(companyId);

        return ApiResponse.<CompanyResponseDTO>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.DELETE_COMPANY_SUCCESS)
                .build();
    }

    @GetMapping("/suggestions")
    public List<CompanySuggestionDTO> suggest(
            @RequestParam String q
    ) {
        return companyService.suggestCompanies(q);
    }
}
