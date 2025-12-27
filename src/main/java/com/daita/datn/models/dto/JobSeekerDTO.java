package com.daita.datn.models.dto;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import com.daita.datn.enums.AccountStatus;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class JobSeekerDTO {
    private Integer jobSeekerId;
    private String accountId;
    private String email;
    private String fullName;
    private LocalDate dob;
    private String phone;
    private String address;
    private String cvUrl;
    private String avatarUrl;
    private String bio;
    private Set<SkillDTO> skills;
    private LocalDateTime createdAt;
    private AccountStatus accountStatus;
}
