package com.daita.datn.models.dto;

import lombok.*;
import com.daita.datn.enums.RecruiterStatus;
import com.daita.datn.enums.AccountStatus;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class RecruiterDTO {
    private Integer recruiterId;
    private String accountId;
    private String email;
    private AccountStatus accountStatus;
    private Integer companyId;
    private String companyName;
    private String position;
    private String phone;
    private RecruiterStatus status;
}
