package com.daita.datn.services;


import com.daita.datn.models.dto.auth.AuthenticationDTO;
import com.daita.datn.models.dto.auth.GoogleLoginRequestDTO;
import com.daita.datn.models.dto.auth.LoginRequestDTO;
import com.daita.datn.models.dto.auth.TokenDTO;

import java.text.ParseException;

public interface AuthService {
    AuthenticationDTO login(LoginRequestDTO request);
    AuthenticationDTO loginWithGoogle(GoogleLoginRequestDTO request);
    void logout(String token) throws ParseException;
    TokenDTO refreshToken(String refreshToken);
}
