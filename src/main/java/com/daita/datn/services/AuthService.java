package com.daita.datn.services;

import com.daita.datn.models.dto.auth.AuthenticationDTO;
import com.daita.datn.models.dto.auth.LoginRequestDTO;
import com.daita.datn.models.dto.auth.TokenDTO;
import com.daita.datn.models.entities.auth.Account;

public interface AuthService {
    TokenDTO refreshToken(String refreshToken);
}
