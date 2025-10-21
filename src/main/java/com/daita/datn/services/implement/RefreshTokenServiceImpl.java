package com.daita.datn.services.implement;

import com.daita.datn.models.entities.auth.RefreshToken;
import com.daita.datn.services.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Override
    public RefreshToken findByRefreshToken(String refreshToken) {
        return null;
    }

    @Override
    public RefreshToken findByRefreshTokenWithAccount(String refreshToken) {
        return null;
    }

    @Override
    public RefreshToken validateRefreshToken(String refreshToken) {
        return null;
    }

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        return null;
    }
}
