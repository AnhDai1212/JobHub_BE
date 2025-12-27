package com.daita.datn.models.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecruiterConsultationRequest {
    @NotBlank
    private String hiringPosition;
        private String industry;
    private Double budget;
    private String currency;
    private String notes;
}
