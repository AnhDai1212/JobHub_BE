package com.daita.datn.models.dto.auth;

import lombok.*;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class PermissionDTO {
    private Integer permissionId;
    private String permissionName;
    private String permissionDescription;
}
