package com.daita.datn.models.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecruiterRegisterResponse {
    private Integer recruiterId;
    private Integer companyId;
    private String companyName;
}
