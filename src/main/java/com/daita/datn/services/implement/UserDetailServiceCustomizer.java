package com.daita.datn.services.implement;

import com.daita.datn.enums.ErrorCode;
import com.daita.datn.exceptions.AppException;
import com.daita.datn.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailServiceCustomizer implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return accountRepository.findByEmailWithRoles(email).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Account "));
    }
}