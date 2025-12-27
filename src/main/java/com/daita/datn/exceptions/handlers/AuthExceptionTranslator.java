package com.daita.datn.exceptions.handlers;

import com.daita.datn.enums.ErrorCode;
import com.daita.datn.exceptions.AppException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AuthExceptionTranslator {

    public RuntimeException translate(AuthenticationException ex) {
        if (ex instanceof BadCredentialsException
                || ex instanceof InternalAuthenticationServiceException
                || ex instanceof UsernameNotFoundException) {
            return new AppException(ErrorCode.INVALID_CREDENTIALS);
        }

        if (ex instanceof LockedException)
            return new AppException(ErrorCode.ACCOUNT_LOCKED);

        if (ex instanceof DisabledException)
            return new AppException(ErrorCode.ACCOUNT_DISABLED);

        return new AppException(ErrorCode.UNAUTHENTICATED, ex.getMessage());
    }
}
