package com.daita.datn.models.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CandidateChatRequest {
    @NotBlank
    private String prompt;
}
