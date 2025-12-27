package com.daita.datn.models.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecruiterProfileResponse {
    private Integer recruiterId;
    private Integer companyId;
    private String companyName;
    private String position;
    private String phone;
    private String avatarUrl;
    private com.daita.datn.enums.RecruiterStatus status;
}
