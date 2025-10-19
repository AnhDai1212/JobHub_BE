package com.daita.datn.services.implement;

import com.daita.datn.enums.ErrorCode;
import com.daita.datn.exceptions.AppException;
import com.daita.datn.models.dto.auth.TokenDTO;
import com.daita.datn.models.dto.auth.TokenPayload;
import com.daita.datn.models.entities.auth.Account;
import com.daita.datn.models.entities.auth.RefreshToken;
import com.daita.datn.services.AuthService;
import com.daita.datn.services.JwtService;
import com.daita.datn.services.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService  {
    private final JwtService jwtService;

    public TokenDTO refreshToken(String refreshToken) {

        Account account  = jwtService.verifyRefreshToken(refreshToken);
        jwtService.revokeRefreshToken(refreshToken);

        TokenPayload newAccessToken = jwtService.generateAccessToken(account);
        TokenPayload newRefreshToken = jwtService.generateRefreshToken(account);

        return TokenDTO.builder()
                .accessToken(newAccessToken.getToken())
                .refreshToken(newRefreshToken.getToken())
                .build();
    }
}
