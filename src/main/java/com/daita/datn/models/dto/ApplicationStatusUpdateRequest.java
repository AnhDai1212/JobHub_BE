package com.daita.datn.models.dto;

import com.daita.datn.enums.ApplicationStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationStatusUpdateRequest {

    @NotNull
    private ApplicationStatus status;

    @Size(max = 1000)
    private String note;
}
