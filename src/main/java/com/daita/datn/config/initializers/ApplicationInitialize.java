package com.daita.datn.config.initializers;

import com.daita.datn.services.AccountService;
import com.daita.datn.services.PasswordService;
import com.daita.datn.services.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tipone.buone.api.vehiclemanagement.enums.RoleType;
import tipone.buone.api.vehiclemanagement.models.entities.Account;
import tipone.buone.api.vehiclemanagement.models.entities.Role;
import tipone.buone.api.vehiclemanagement.services.AccountService;
import tipone.buone.api.vehiclemanagement.services.PasswordService;
import tipone.buone.api.vehiclemanagement.services.RoleService;

import java.util.Objects;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApplicationInitialize implements CommandLineRunner {
    private static final String ADMIN_EMAIL = "admin@tma.com.vn";
    private static final String ADMIN_PASSWORD = "123qweQWE";
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
            Account account = new Account();
            account.setEmail(ADMIN_EMAIL);
            account.setPassword(passwordService.encryptPassword(ADMIN_PASSWORD));
            account.setActive(true);
            account.setRoles(Set.of(Objects.requireNonNull(role)));
            accountService.save(account);
    }catch (Exception e){
            log.error("Failed to create default admin account", e);
        }
    }
}
