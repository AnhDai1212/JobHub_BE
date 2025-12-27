package com.daita.datn.services.implement;

import com.daita.datn.enums.ErrorCode;
import com.daita.datn.enums.RoleType;
import com.daita.datn.exceptions.AppException;
import com.daita.datn.models.dto.auth.JwtInfo;
import com.daita.datn.models.dto.auth.TokenDTO;
import com.daita.datn.models.dto.auth.TokenPayload;
import com.daita.datn.models.entities.auth.Account;
import com.daita.datn.models.entities.auth.RedisToken;
import com.daita.datn.models.entities.auth.Role;
import com.daita.datn.repositories.AccountRepository;
import com.daita.datn.repositories.RedisTokenRepository;
import com.daita.datn.services.JwtService;
import com.daita.datn.services.RedisService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    @Value("${jwt.signerkey}")
    private String secretKey;

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    private final AccountRepository accountRepository;

    private final RedisService redisService;


    @Override
    public TokenPayload generateAccessToken(String accountId, String email, Set<RoleType> roleType) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        Date issueTime = new Date();
        Date expirationTime = new Date(issueTime.getTime() + accessTokenExpiration);
        String jwtId = UUID.randomUUID().toString();
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .jwtID(jwtId)
                .subject(accountId)
                .claim("email", email)
                .claim("role", roleType)
                .issueTime(issueTime)
                .expirationTime(expirationTime)
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        try {
            jwsObject.sign(new MACSigner(secretKey));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String token = jwsObject.serialize();
        return TokenPayload.builder()
                .token(token)
                .jwtID(jwtId)
                .expiredTime(expirationTime)
                .build();
    }

    @Override
    public TokenPayload generateRefreshToken(String accountId) {
        String refreshToken = UUID.randomUUID().toString();
        RedisToken token = redisService.saveToken(RedisToken.builder()
                .jwtId(refreshToken)
                .accountId(accountId)
                .expiredTime(refreshTokenExpiration / 1000)
                .build());

        System.out.println("Saved token key: " + token);

        return TokenPayload.builder()
                .token(refreshToken)
                .expiredTime(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .build();
    }

    @Override
    public boolean verifyToken(String token) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        if (expirationTime.before(new Date())) {
            return false;
        }

        String jwtId = signedJWT.getJWTClaimsSet().getJWTID();
        Optional<RedisToken> tokenInRedis = redisService.findById(jwtId);
        if (tokenInRedis.isPresent()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return signedJWT.verify(new MACVerifier(secretKey));
    }

    @Override
    public JwtInfo parseToken(String token) throws ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        String jwtID = signedJWT.getJWTClaimsSet().getJWTID();
        Date issueTime = signedJWT.getJWTClaimsSet().getIssueTime();
        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        return JwtInfo.builder()
                .jwtID(jwtID)
                .issueTime(issueTime)
                .expiredTime(expirationTime)
                .build();
    }

    @Override
    public TokenDTO generateFor(Account account) {
        var roles = account.getRoles()
                .stream().map(Role::getRoleName).collect(Collectors.toSet());
        var accessPayloadToken = generateAccessToken(account.getAccountId(), account.getEmail(), roles);
        var refreshPayloadToken = generateRefreshToken(account.getAccountId());
        return TokenDTO.builder()
                .accessToken(accessPayloadToken.getToken())
                .refreshToken(refreshPayloadToken.getToken())
                .build();
    }

    @Override
    public Account verifyRefreshToken(String refreshToken) {
        System.out.println("Refresh Token: " + refreshToken);
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        RedisToken token = redisService.findById(refreshToken)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_TOKEN));
        return accountRepository.findById(token.getAccountId())
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
    }

    @Override
    public void revokeRefreshToken(String refreshToken) {
        System.out.println("Trying to revoke token: " + refreshToken);
        System.out.println("Exists in Redis? " + redisService.existsById(refreshToken));
        if (redisService.existsById(refreshToken)) {
            redisService.deleteById(refreshToken);
        }
    }
}
