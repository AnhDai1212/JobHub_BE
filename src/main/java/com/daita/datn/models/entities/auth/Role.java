package com.daita.datn.models.entities.auth;

import com.daita.datn.enums.RoleType;
import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Table(name = "roles")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer roleId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name", nullable = false, unique = true, length = 50)
    private RoleType roleName;

    @Column(name = "role_description", length = 100)
    private String roleDescription;

    @ManyToMany(mappedBy = "roles")
    private Set<Account> accounts;
}