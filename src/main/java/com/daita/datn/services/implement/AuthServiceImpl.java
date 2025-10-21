package com.daita.datn.services.implement;

import com.daita.datn.enums.RoleType;
import com.daita.datn.models.dto.auth.TokenDTO;
import com.daita.datn.models.dto.auth.TokenPayload;
import com.daita.datn.models.entities.auth.Account;
import com.daita.datn.models.entities.auth.Role;
import com.daita.datn.services.AuthService;
import com.daita.datn.services.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtService jwtService;

    public TokenDTO refreshToken(String refreshToken) {

        Account account = jwtService.verifyRefreshToken(refreshToken);
        jwtService.revokeRefreshToken(refreshToken);

        Set<RoleType> roleNames = account.getRoles().stream()
                .map(Role::getRoleName)
                .collect(Collectors.toSet());

        TokenPayload newAccessToken = jwtService.generateAccessToken(account.getAccountId(), account.getEmail(), roleNames);
        TokenPayload newRefreshToken = jwtService.generateRefreshToken(account.getAccountId());

        return TokenDTO.builder()
                .accessToken(newAccessToken.getToken())
                .refreshToken(newRefreshToken.getToken())
                .build();
    }
}
