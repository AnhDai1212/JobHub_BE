package com.daita.datn.models.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationCreateRequest {
    @NotNull
    private Integer jobId;
}
