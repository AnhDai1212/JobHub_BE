package com.daita.datn.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CvParseResponse {
    private String fileKey;
    private String rawText;
    private Object parsedData;
}
