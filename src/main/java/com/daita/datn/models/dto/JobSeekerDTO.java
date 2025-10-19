package com.daita.datn.models.dto;

import lombok.*;
import java.time.LocalDate;
import java.util.Set;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class JobSeekerDTO {
    private Integer jobSeekerId;
    private String accountId;
    private String fullName;
    private LocalDate dob;
    private String phone;
    private String address;
    private String cvUrl;
    private String bio;
    private Set<SkillDTO> skills;
}
