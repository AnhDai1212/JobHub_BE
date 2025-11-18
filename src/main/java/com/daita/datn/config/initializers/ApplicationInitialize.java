package com.daita.datn.config.initializers;

import com.daita.datn.enums.AccountStatus;
import com.daita.datn.enums.RoleType;
import com.daita.datn.models.entities.auth.Account;
import com.daita.datn.models.entities.auth.Role;
import com.daita.datn.services.AccountService;
import com.daita.datn.services.PasswordService;
import com.daita.datn.services.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


import java.util.Objects;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApplicationInitialize implements CommandLineRunner {
    private static final String ADMIN_EMAIL = "admin";
    private static final String ADMIN_PASSWORD = "Admin123@";
    private final AccountService accountService;
    private final RoleService roleService;
    private final PasswordService passwordService;

    @Override
    public void run(String... args) throws Exception {
        try {
            if (accountService.existsByEmail(ADMIN_EMAIL))
                return;
            if (accountService.existsByRole(RoleType.ADMIN))
                return;
            Role role = roleService.getByType(RoleType.ADMIN).orElse(null);
            Account account = Account.builder()
                    .email(ADMIN_EMAIL)
                    .password(passwordService.encryptPassword(ADMIN_PASSWORD))
                    .status(AccountStatus.ACTIVE)
                    .roles(Set.of(Objects.requireNonNull(role)))
                    .build();
            accountService.save(account);
    }catch (Exception e){
            log.error("Failed to create default admin account", e);
        }
    }
}
