package com.daita.datn.models.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class RecommendationDTO {
    private String recommendationId;
    private Integer jobSeekerId;
    private Integer jobId;
    private BigDecimal score;
    private LocalDateTime generatedAt;
}
