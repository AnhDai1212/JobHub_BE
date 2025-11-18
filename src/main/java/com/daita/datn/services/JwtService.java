package com.daita.datn.services;

import com.daita.datn.enums.RoleType;
import com.daita.datn.models.dto.auth.JwtInfo;
import com.daita.datn.models.dto.auth.TokenDTO;
import com.daita.datn.models.dto.auth.TokenPayload;
import com.daita.datn.models.entities.auth.Account;
import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.HttpServletRequest;

import java.text.ParseException;
import java.util.Set;

public interface JwtService {
    TokenPayload generateAccessToken(String accountId, String email, Set<RoleType> roleNames);
    TokenPayload generateRefreshToken(String accountId);
    Account verifyRefreshToken(String refreshToken);
    void revokeRefreshToken(String refreshToken);
    boolean verifyToken(String token) throws ParseException, JOSEException;
    JwtInfo parseToken(String token) throws ParseException;
    TokenDTO generateFor(Account account);
}