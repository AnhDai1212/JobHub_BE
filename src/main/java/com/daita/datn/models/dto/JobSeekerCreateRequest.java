package com.daita.datn.models.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class JobSeekerCreateRequest {
    @NotBlank
    private String fullName;
    private LocalDate dob;
    private String phone;
    private String address;
    private String cvUrl;
    private String bio;
}
