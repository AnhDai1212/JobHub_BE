package com.daita.datn.models.dto;

import lombok.*;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class RecruiterDTO {
    private Integer recruiterId;
    private String accountId;
    private Integer companyId;
    private String position;
    private String phone;
}
