package com.daita.datn.models.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class ParsedCvDTO {
    private String cvId;
    private Integer jobSeekerId;
    private String fileUrl;
    private String extractedText;
    private String embedding; // JSON string
    private Object parsedData; // JSON object for FE convenience
    private LocalDateTime createdAt;
}
