package com.daita.datn.models.dto;

import com.daita.datn.enums.JobStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobStatusUpdateRequest {

    @NotNull
    private JobStatus status;
}
