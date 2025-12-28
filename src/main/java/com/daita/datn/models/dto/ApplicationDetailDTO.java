package com.daita.datn.models.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ApplicationDetailDTO {
    private String applicationId;
    private Integer jobId;
    private String jobTitle;
    private Integer jobSeekerId;
    private LocalDateTime appliedAt;
    private String status;
    private String parsedCvId;
    private String parsedCvJson;
    private Double matchingScore;
    private List<ApplicationHistoryDTO> histories;
}
