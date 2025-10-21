package com.daita.datn.services;

import com.daita.datn.models.dto.auth.TokenDTO;

public interface AuthService {
    TokenDTO refreshToken(String refreshToken);
}
