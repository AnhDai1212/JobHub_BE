package com.daita.datn.models.dto;

import com.daita.datn.enums.AccountStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountStatusUpdateRequest {

    @NotNull
    private AccountStatus status;
}
