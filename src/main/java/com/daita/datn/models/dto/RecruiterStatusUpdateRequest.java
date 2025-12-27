package com.daita.datn.models.dto;

import com.daita.datn.enums.RecruiterStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecruiterStatusUpdateRequest {

    @NotNull
    private RecruiterStatus status;
}
