package com.daita.datn.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecruiterDocumentDTO {
    private Long documentId;
    private String fileKey;
    private String downloadUrl;
    private String fileName;
    private String contentType;
}
