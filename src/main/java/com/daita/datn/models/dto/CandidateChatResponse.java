package com.daita.datn.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateChatResponse {
    private String applicationId;
    private Integer jobSeekerId;
    private String fullName;
    private Double matchingScore;
    private String reason;
}
