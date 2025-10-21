package com.daita.datn.models.dto.auth;

import lombok.*;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class LoginRequestDTO {
    private String email;
    private String password;
}
