package com.daita.datn.services;

import com.daita.datn.models.entities.auth.Role;
import tipone.buone.api.vehiclemanagement.enums.RoleType;
import tipone.buone.api.vehiclemanagement.models.entities.Role;

import java.util.Optional;

public interface RoleService {
    Optional<Role> getByType(RoleType roleType);
}
