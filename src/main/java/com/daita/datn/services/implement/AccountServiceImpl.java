package com.daita.datn.services.implement;

import com.daita.datn.enums.ErrorCode;
import com.daita.datn.enums.RoleType;
import com.daita.datn.exceptions.AppException;
import com.daita.datn.exceptions.handlers.AuthExceptionTranslator;
import com.daita.datn.models.dto.UpgradeRecruiterDTO;
import com.daita.datn.models.dto.auth.*;
import com.daita.datn.models.entities.Company;
import com.daita.datn.models.entities.Recruiter;
import com.daita.datn.models.entities.auth.Account;
import com.daita.datn.models.entities.auth.Role;
import com.daita.datn.models.mappers.AccountMapper;
import com.daita.datn.repositories.AccountRepository;
import com.daita.datn.services.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Duration;
import java.util.Date;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AccountServiceImpl implements AccountService {

    AccountRepository accountRepository;
    PasswordEncoder passwordEncoder;
    OtpService otpService;
    RoleService roleService;
    RedisTemplate<String, Object> redisTemplate;
    EmailService emailService;
    AccountMapper accountMapper;

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

        Account account = accountMapper.mapRegisterDtoToEntity(cachedRegistration, null);
        accountRepository.save(account);

        redisTemplate.delete("register:" + email);
        otpService.deleteOtp(email);

        log.info("Account registered successfully for {}", email);
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
    public void verifyOtp(OtpVerificationDTO dto) {
        String email = dto.getEmail();
        String cachedOtp = otpService.getOtp(email);

        if (cachedOtp != null && otpService.verifyOtp(dto.getOtp(), cachedOtp)) {
            otpService.deleteOtp(email);
            redisTemplate.opsForValue().set("verified:" + email, true, Duration.ofMinutes(10));
            log.info("OTP verified successfully for {}", email);
        } else {
            throw new AppException(ErrorCode.INVALID_OTP);
        }
    }

    @Override
    public void updatePassword(UpdatePasswordDTO dto) {
        String email = dto.getEmail();

        Boolean verified = (Boolean) redisTemplate.opsForValue().get("verified:" + email);
        if (verified == null || !verified) {
            throw new AppException(ErrorCode.OTP_NOT_VERIFIED);
        }

        Account account = findByEmail(email);
        String password = passwordEncoder.encode(dto.getNewPassword());
        account.setPassword(password);
        accountRepository.save(account);

        redisTemplate.delete("verified:" + email);

        log.info("Password updated successfully for {}", email);
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

    @Override
    public void upgradeToRecruiter(UpgradeRecruiterDTO dto) {

        Jwt jwt = (Jwt) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        String accountId = jwt.getSubject();

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AppException(
                        ErrorCode.RESOURCE_NOT_FOUND, "Account"
                ));

        Role recruiterRole = roleService.getByType(RoleType.RECRUITER)
                .orElseThrow(() -> new AppException(
                        ErrorCode.RESOURCE_NOT_FOUND, "Role RECRUITER"
                ));

        account.getRoles().add(recruiterRole);
        accountRepository.save(account);
    }

    @Override
    public Account getCurrentAccount() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        String accountId = authentication.getName();

        return accountRepository.findById(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND,"ACCOUNT"));
    }
}
