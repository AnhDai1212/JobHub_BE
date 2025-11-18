package com.daita.datn.services.implement;

import com.daita.datn.models.dto.auth.TokenDTO;
import com.daita.datn.models.entities.auth.Account;
import com.daita.datn.services.AuthService;
import com.daita.datn.services.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtService jwtService;

    public TokenDTO refreshToken(String refreshToken) {

        Account account = jwtService.verifyRefreshToken(refreshToken);
        jwtService.revokeRefreshToken(refreshToken);

        TokenDTO token = jwtService.generateFor(account);

        return TokenDTO.builder()
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .build();
    }
}
