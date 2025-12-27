package com.daita.datn.services;

import com.daita.datn.enums.RoleType;
import com.daita.datn.models.entities.auth.Account;
import com.daita.datn.models.entities.auth.Role;


import java.util.Optional;

public interface RoleService {
    Optional<Role> getByType(RoleType roleType);
    void assignRole(Account account, RoleType roleType);
}
