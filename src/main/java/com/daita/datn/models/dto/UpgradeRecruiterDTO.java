package com.daita.datn.models.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpgradeRecruiterDTO {

    private Integer companyId;
    private String companyName;
    private String location;
    private String website;
    private String introduction;
    private String position;
    private String phone;
}
