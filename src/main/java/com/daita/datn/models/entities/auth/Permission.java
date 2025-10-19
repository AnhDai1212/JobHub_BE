package com.daita.datn.models.entities.auth;

import com.daita.datn.models.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Table(name = "permissions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Permission extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id")
    private Integer permissionId;

    @Column(name = "permission_name", nullable = false, unique = true, length = 50)
    private String permissionName;

    @Column(name = "permission_description", length = 100)
    private String permissionDescription;

    @ManyToMany(mappedBy = "permissions")
    private Set<Role> roles;
}
