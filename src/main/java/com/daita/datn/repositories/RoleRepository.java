package com.daita.datn.repositories;

import com.daita.datn.enums.RoleType;
import com.daita.datn.models.entities.auth.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findRoleByRoleName(RoleType roleName);
}