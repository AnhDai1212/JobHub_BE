package com.daita.datn.services.implement;

import com.daita.datn.enums.ErrorCode;
import com.daita.datn.enums.RoleType;
import com.daita.datn.exceptions.AppException;
import com.daita.datn.exceptions.handlers.AuthExceptionTranslator;
import com.daita.datn.models.dto.auth.*;
import com.daita.datn.models.entities.auth.Account;
import com.daita.datn.models.entities.auth.RedisToken;
import com.daita.datn.models.entities.auth.Role;
import com.daita.datn.models.mappers.AccountMapper;
import com.daita.datn.repositories.AccountRepository;
import com.daita.datn.repositories.RedisTokenRepository;
import com.daita.datn.services.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Duration;
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
    OtpService otpService;
    RoleService roleService;
    RedisTemplate<String, Object> redisTemplate;
    EmailService emailService;
    AuthExceptionTranslator authExceptionTranslator;

    @Override
    public AuthenticationDTO login(LoginRequestDTO request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            Account account = (Account) authentication.getPrincipal();

            TokenDTO tokenPair  = jwtService.generateFor(account);

            return AuthenticationDTO.builder()
                    .accessToken(tokenPair.getAccessToken())
                    .refreshToken(tokenPair.getRefreshToken())
                    .account(accountMapper.toDTO(account))
                    .build();

        } catch (AuthenticationException  ex) {
            throw authExceptionTranslator.translate(ex);
        }
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
        log.info("Logout successfully");
    }

    @Override
    public void forgotPassword(ForgotPasswordDTO dto) {
        String email = dto.getEmail();
        findByEmail(email);

        if (otpService.isOtpLimitExceeded(email)) {
            throw new AppException(ErrorCode.OTP_LIMIT_EXCEEDED, "OTP");
        }

        otpService.deleteOtp(email);
        otpService.increaseRequestCount(email);

        emailService.sendOtpByEmail(email);
        log.info("OTP sent to {}", email);
    }

    @Override
    public void verifyOtp(OtpVerificationDTO otpVerificationDTO) {
        String email = otpVerificationDTO.getEmail();
        String cachedOtp = otpService.getOtp(email);

        if (cachedOtp != null && otpService.verifyOtp(otpVerificationDTO.getOtp(), cachedOtp)) {
            otpService.deleteOtp(email);

            redisTemplate.opsForValue().set("verified:" + email, true, Duration.ofMinutes(10));
            log.info("OTP verified successfully for {}", email);
        } else {
            throw new AppException(ErrorCode.INVALID_OTP);
        }
    }

    @Override
    public void updatePassword(UpdatePasswordDTO updatePasswordDTO) {
        String email = updatePasswordDTO.getEmail();

        Boolean verified = (Boolean) redisTemplate.opsForValue().get("verified:" + email);
        if (verified == null || !verified) {
            throw new AppException(ErrorCode.OTP_NOT_VERIFIED);
        }

        Account account = findByEmail(email);
        String password = passwordEncoder.encode(updatePasswordDTO.getNewPassword());
        account.setPassword(password);
        accountRepository.save(account);

        redisTemplate.delete("verified:" + email);
        log.info("Password updated successfully for {}", email);
    }

    @Override
    public void register(RegisterRequestDTO requestDTO) {
        String email = requestDTO.getEmail();

        if (accountRepository.existsByEmail(email)) {
            throw new AppException(ErrorCode.RESOURCE_ALREADY_EXISTS, "Email already exists");
        }

        if (otpService.isOtpLimitExceeded(email)) {
            throw new AppException(ErrorCode.OTP_LIMIT_EXCEEDED, "OTP limit reached");
        }

        String hashedPassword = passwordEncoder.encode(requestDTO.getPassword());
        requestDTO.setPassword(hashedPassword);
        redisTemplate.opsForValue().set("register:" + email, requestDTO, Duration.ofMinutes(10));

        emailService.sendOtpByEmail(email);
        otpService.increaseRequestCount(email);
        log.info("Registration OTP sent to {}", email);
    }

    @Override
    public void verifyRegistrationByOtp(OtpVerificationDTO otpVerificationDTO) {
        String email = otpVerificationDTO.getEmail();
        RegisterRequestDTO cachedRegistration =
                (RegisterRequestDTO) redisTemplate.opsForValue().get("register:" + email);

        if (cachedRegistration == null) {
            otpService.deleteOtp(email);
            throw new AppException(ErrorCode.INVALID_OTP, "Registration expired");
        }

        String cachedOtp = otpService.getOtp(email);
        if (cachedOtp == null || !otpService.verifyOtp(otpVerificationDTO.getOtp(), cachedOtp)) {
            throw new AppException(ErrorCode.INVALID_OTP, "Invalid registration OTP");
        }

        Role userRole = roleService.getByType(RoleType.JOB_SEEKER)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Role JOB_SEEKER not found"));

        Account account = accountMapper.mapRegisterDtoToEntity(cachedRegistration, userRole);
        accountRepository.save(account);

        redisTemplate.delete("register:" + email);
        otpService.deleteOtp(email);

        log.info("Account registered successfully for {}", email);
    }

    @Override
    public Account findByEmail(String email) {
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Email not found"));
    }

    @Override
    public boolean existsByEmail(String email) {
        return accountRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByRole(RoleType roleType) {
        return accountRepository.existsByRoles_RoleName(roleType);
    }

    @Override
    public void save(Account account) {
        accountRepository.save(account);
    }
}
