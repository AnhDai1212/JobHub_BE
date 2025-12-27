package com.daita.datn.services.implement;

import com.daita.datn.enums.AccountStatus;
import com.daita.datn.enums.ErrorCode;
import com.daita.datn.enums.RoleType;
import com.daita.datn.exceptions.AppException;
import com.daita.datn.exceptions.handlers.AuthExceptionTranslator;
import com.daita.datn.models.dto.UpgradeRecruiterDTO;
import com.daita.datn.models.dto.auth.AuthenticationDTO;
import com.daita.datn.models.dto.auth.GoogleLoginRequestDTO;
import com.daita.datn.models.dto.auth.JwtInfo;
import com.daita.datn.models.dto.auth.LoginRequestDTO;
import com.daita.datn.models.dto.auth.RegisterRequestDTO;
import com.daita.datn.models.dto.auth.TokenDTO;
import com.daita.datn.models.entities.auth.Account;
import com.daita.datn.models.entities.auth.RedisToken;
import com.daita.datn.models.entities.auth.Role;
import com.daita.datn.models.mappers.AccountMapper;
import com.daita.datn.repositories.AccountRepository;
import com.daita.datn.repositories.RedisTokenRepository;
import com.daita.datn.services.AuthService;
import com.daita.datn.services.JwtService;
import com.daita.datn.services.RoleService;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthServiceImpl implements AuthService {

    AuthenticationManager authenticationManager;
    JwtServiceImpl jwtService;
    RedisTokenRepository redisTokenRepository;
    AccountRepository accountRepository;
    PasswordEncoder passwordEncoder;
    AuthExceptionTranslator authExceptionTranslator;
    AccountMapper accountMapper;
    private final RoleService roleService;

    @NonFinal
    @Value("${google.oauth.client-id}")
    String googleClientId;

    @NonFinal
    @Value("${google.oauth.issuer}")
    String googleIssuer;

    @NonFinal
    @Value("${google.oauth.jwk-set-uri}")
    String googleJwkSetUri;

    @NonFinal
    JwtDecoder googleJwtDecoder;

    @NonFinal
    @PostConstruct
    void initGoogleDecoder() {
        googleJwtDecoder = NimbusJwtDecoder.withJwkSetUri(googleJwkSetUri).build();
    }

    @Override
    public AuthenticationDTO login(LoginRequestDTO request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            Account account = (Account) authentication.getPrincipal();

            TokenDTO tokenPair = jwtService.generateFor(account);

            return AuthenticationDTO.builder()
                    .accessToken(tokenPair.getAccessToken())
                    .refreshToken(tokenPair.getRefreshToken())
                    .account(accountMapper.toDTO(account))   // GIỮ NGUYÊN
                    .build();

        } catch (AuthenticationException ex) {
            throw authExceptionTranslator.translate(ex);   // GIỮ NGUYÊN
        }
    }

    @Override
    public AuthenticationDTO loginWithGoogle(GoogleLoginRequestDTO request) {
        Jwt jwt = decodeGoogleToken(request.getIdToken());

        String email = jwt.getClaimAsString("email");
        Boolean emailVerified = jwt.getClaim("email_verified");
        if (email == null || email.isBlank() || emailVerified == null || !emailVerified) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        Account account = accountRepository.findByEmailWithRoles(email)
                .orElseGet(() -> createAccountForGoogle(email));

        validateAccountStatus(account);

        TokenDTO tokenPair = jwtService.generateFor(account);

        return AuthenticationDTO.builder()
                .accessToken(tokenPair.getAccessToken())
                .refreshToken(tokenPair.getRefreshToken())
                .account(accountMapper.toDTO(account))
                .build();
    }

    @Override
    public void logout(String token) throws ParseException {
        JwtInfo jwtInfo = jwtService.parseToken(token);
        String jwtId = jwtInfo.getJwtID();
        Date issueTime = jwtInfo.getIssueTime();
        Date expiredTime = jwtInfo.getExpiredTime();

        if (expiredTime.before(new Date())) {
            return;
        }

        RedisToken redisToken = RedisToken.builder()
                .jwtId(jwtId)
                .expiredTime((expiredTime.getTime() - issueTime.getTime()) / 1000)
                .build();

        redisTokenRepository.save(redisToken);

        log.info("Logout successfully");   // GIỮ NGUYÊN
    }

    @Override
    public TokenDTO refreshToken(String refreshToken) {

        Account account = jwtService.verifyRefreshToken(refreshToken);
        jwtService.revokeRefreshToken(refreshToken);

        TokenDTO token = jwtService.generateFor(account);

        return TokenDTO.builder()
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .build();
    }

    private Jwt decodeGoogleToken(String idToken) {
        try {
            Jwt jwt = googleJwtDecoder.decode(idToken);
            if (jwt.getIssuer() == null || !googleIssuer.equals(jwt.getIssuer().toString())) {
                throw new AppException(ErrorCode.INVALID_TOKEN);
            }
            if (jwt.getAudience() == null || !jwt.getAudience().contains(googleClientId)) {
                throw new AppException(ErrorCode.INVALID_TOKEN);
            }
            return jwt;
        } catch (JwtException ex) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
    }

    private Account createAccountForGoogle(String email) {
        Role defaultRole = roleService.getByType(RoleType.JOB_SEEKER)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Role"));
        String randomPassword = passwordEncoder.encode(UUID.randomUUID().toString());
        RegisterRequestDTO registerRequest = RegisterRequestDTO.builder()
                .email(email)
                .password(randomPassword)
                .build();
        Account account = accountMapper.mapRegisterDtoToEntity(registerRequest, defaultRole);
        return accountRepository.save(account);
    }

    private void validateAccountStatus(Account account) {
        if (account.getStatus() == AccountStatus.LOCKED) {
            throw new AppException(ErrorCode.ACCOUNT_LOCKED);
        }
        if (account.getStatus() == AccountStatus.INACTIVE) {
            throw new AppException(ErrorCode.ACCOUNT_DISABLED);
        }
    }
}

