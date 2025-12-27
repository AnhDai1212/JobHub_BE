package com.daita.datn.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParsedCvSaveRequest {
    @NotBlank
    private String fileKey;

    @NotBlank
    private String rawText;

    @NotNull
    private Object parsedData;
}
