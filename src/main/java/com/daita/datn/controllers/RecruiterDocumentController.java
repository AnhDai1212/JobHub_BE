package com.daita.datn.controllers;

import com.daita.datn.common.base.ApiResponse;
import com.daita.datn.common.constants.MessageConstant;
import com.daita.datn.models.entities.RecruiterDocument;
import com.daita.datn.models.dto.RecruiterDocumentDTO;
import com.daita.datn.services.RecruiterDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/recruiters/documents")
@RequiredArgsConstructor
@PreAuthorize("hasRole('RECRUITER')")
public class RecruiterDocumentController {

    private final RecruiterDocumentService recruiterDocumentService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<RecruiterDocumentDTO> upload(
            @RequestPart("file") MultipartFile file
    ) throws IOException {
        RecruiterDocumentDTO doc = recruiterDocumentService.uploadForCurrentRecruiter(file);
        return ApiResponse.<RecruiterDocumentDTO>builder()
                .code(HttpStatus.CREATED.value())
                .status(HttpStatus.CREATED.getReasonPhrase())
                .message(MessageConstant.DOCUMENT_UPLOAD_SUCCESS)
                .data(doc)
                .build();
    }

    @GetMapping
    public ApiResponse<List<RecruiterDocumentDTO>> list() {
        List<RecruiterDocumentDTO> docs = recruiterDocumentService.listForCurrentRecruiter();
        return ApiResponse.<List<RecruiterDocumentDTO>>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.DOCUMENT_LIST_SUCCESS)
                .data(docs)
                .build();
    }
}
