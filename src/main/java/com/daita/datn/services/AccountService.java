package com.daita.datn.services;

import com.daita.datn.enums.RoleType;
import com.daita.datn.models.dto.auth.*;
import com.daita.datn.models.entities.auth.Account;
import com.nimbusds.jose.JOSEException;

import java.io.IOException;
import java.text.ParseException;

public interface AccountService {
    void register(RegisterRequestDTO requestDTO);
    AuthenticationDTO login(LoginRequestDTO requestDTO);
    void logout(String token) throws IOException, JOSEException, ParseException;
    void forgotPassword(ForgotPasswordDTO resendOtpDTO);
    void verifyOtp(OtpVerificationDTO otpVerificationDTO);
    void updatePassword(UpdatePasswordDTO updatePasswordDTO);
    Account findByEmail(String email);
    void verifyRegistrationByOtp(OtpVerificationDTO otpVerificationDTO);
    boolean existsByEmail(String email);
    boolean existsByRole(RoleType roleType);
    void save(Account account);}
