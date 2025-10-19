package com.daita.datn.models.dto.auth;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {
    private String email;
    private String password;
//    private String role; // JOB_SEEKER or RECRUITER
}
