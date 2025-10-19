package com.daita.datn.services.implement;

import com.daita.datn.enums.ErrorCode;
import com.daita.datn.exceptions.AppException;
import com.daita.datn.models.dto.auth.*;
import com.daita.datn.models.entities.auth.Account;
import com.daita.datn.models.entities.auth.RedisToken;
import com.daita.datn.models.mappers.AccountMapper;
import com.daita.datn.repositories.AccountRepository;
import com.daita.datn.repositories.RedisTokenRepository;
import com.daita.datn.services.AccountService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountServiceImpl implements AccountService {

    AccountRepository accountRepository;
    PasswordEncoder passwordEncoder;
    AuthenticationManager authenticationManager;
    JwtServiceImpl jwtService;
    RedisTokenRepository redisTokenRepository;
    AccountMapper accountMapper;

    @Override
    public AuthenticationDTO login(LoginRequestDTO request) throws UsernameNotFoundException {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        Authentication authentication = authenticationManager.authenticate(token);

        Account account = (Account) authentication.getPrincipal();
        TokenPayload accessPayloadToken = jwtService.generateAccessToken(account);
        TokenPayload refreshPayloadToken = jwtService.generateRefreshToken(account);

        return AuthenticationDTO.builder()
                .accessToken(accessPayloadToken.getToken())
                .refreshToken(refreshPayloadToken.getToken())
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
        log.info("logout successfully");
    }

    @Override
    public void register(RegisterRequestDTO requestDTO) {
        if (accountRepository.existsByUsername(requestDTO.getUsername()))
            throw new AppException(ErrorCode.RESOURCE_ALREADY_EXISTS, "User ");
        Account account = new Account();
        account.setUsername(requestDTO.getUsername());
        account.setEmail(requestDTO.getEmail());
        account.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        accountRepository.save(account);
    }

}
