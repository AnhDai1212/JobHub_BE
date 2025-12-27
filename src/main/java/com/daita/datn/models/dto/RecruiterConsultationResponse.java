package com.daita.datn.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecruiterConsultationResponse {
    private Long consultationId;
    private String hiringPosition;
    private String industry;
    private Double budget;
    private String currency;
    private String notes;
}
