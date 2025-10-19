package com.daita.datn.models.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class ApplicationDTO {
    private String applicationId;
    private Integer jobId;
    private Integer jobSeekerId;
    private LocalDateTime appliedAt;
    private String status;
}
