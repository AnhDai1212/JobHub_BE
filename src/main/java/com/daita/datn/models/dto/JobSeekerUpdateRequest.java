package com.daita.datn.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobSeekerUpdateRequest {
    private String fullName;
    private LocalDate dob;
    private String phone;
    private String address;
    private String bio;
}
