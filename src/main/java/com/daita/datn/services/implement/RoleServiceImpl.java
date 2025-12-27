package com.daita.datn.services.implement;

import com.daita.datn.enums.ErrorCode;
import com.daita.datn.enums.RoleType;
import com.daita.datn.exceptions.AppException;
import com.daita.datn.models.entities.auth.Account;
import com.daita.datn.models.entities.auth.Role;
import com.daita.datn.repositories.RoleRepository;
import com.daita.datn.services.RoleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Optional<Role> getByType(RoleType roleType) {
        return roleRepository.findRoleByRoleName(roleType);
    }

    @Override
    @Transactional
    public void assignRole(Account account, RoleType roleType) {

        Role role = roleRepository.findRoleByRoleName(roleType)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Role"));

        if (account.getRoles().contains(role)) {
            return;
        }

        account.getRoles().add(role);
    }
}
