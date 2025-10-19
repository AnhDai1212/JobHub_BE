package com.daita.datn.models.dto;

import com.daita.datn.models.dto.auth.RoleDTO;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Set;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class AccountDTO {
    private String accountId;
    private String email;
    private String status;
    private LocalDateTime createdAt;
    private Set<RoleDTO> roles;
}
