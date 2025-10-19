package com.daita.datn.services;

import com.daita.datn.models.dto.auth.JwtInfo;
import com.daita.datn.models.dto.auth.TokenPayload;
import com.daita.datn.models.entities.auth.Account;
import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.HttpServletRequest;

import java.text.ParseException;
import java.util.Set;

public interface JwtService {
    TokenPayload generateAccessToken(Account account);
    TokenPayload generateRefreshToken(Account account);
    Account verifyRefreshToken(String refreshToken);
    void revokeRefreshToken(String refreshToken);
    String getToken(HttpServletRequest request);
    boolean verifyToken(String token) throws ParseException, JOSEException;
    JwtInfo parseToken(String token) throws ParseException;
    String extractId(String token);
    Set<String> extractRoles(String token);
    String extractEmail(String token);

}
