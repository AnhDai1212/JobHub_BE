package com.daita.datn.services.implement;

import com.daita.datn.enums.RoleType;
import com.daita.datn.models.entities.auth.Role;
import com.daita.datn.repositories.RoleRepository;
import com.daita.datn.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Optional<Role> getByType(RoleType roleType) {
        return roleRepository.findByName(roleType);
    }
}
