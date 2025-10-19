package com.daita.datn.services;

import com.daita.datn.models.entities.auth.Account;
import com.daita.datn.models.entities.auth.RefreshToken;

public interface RefreshTokenService {
    String generateRefreshToken(Account account);
    RefreshToken findByRefreshToken(String refreshToken);
    RefreshToken findByRefreshTokenWithAccount(String refreshToken);
    RefreshToken validateRefreshToken(String refreshToken);
    RefreshToken save(RefreshToken refreshToken);
}
