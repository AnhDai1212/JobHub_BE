package com.daita.datn.models.dto.auth;

import lombok.*;
import java.util.Set;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class RoleDTO {
    private Integer roleId;
    private String roleName;
    private String roleDescription;
    private Set<PermissionDTO> permissions;
}
