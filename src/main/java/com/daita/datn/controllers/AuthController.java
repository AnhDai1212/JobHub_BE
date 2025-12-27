package com.daita.datn.controllers;

import com.daita.datn.common.base.ApiResponse;
import com.daita.datn.common.constants.MessageConstant;
import com.daita.datn.models.dto.UpgradeRecruiterDTO;
import com.daita.datn.models.dto.auth.*;
import com.daita.datn.services.AccountService;
import com.daita.datn.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AccountService accountService;

    // =========================
    // AUTH (TOKEN / LOGIN)
    // =========================

    @PostMapping("/login")
    public ApiResponse<AuthenticationDTO> login(
            @RequestBody LoginRequestDTO dto
    ) {
        AuthenticationDTO result = authService.login(dto);
        return ApiResponse.<AuthenticationDTO>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.LOGIN_SUCCESS)
                .data(result)
                .build();
    }

    @PostMapping("/google-login")
    public ApiResponse<AuthenticationDTO> googleLogin(
            @Valid @RequestBody GoogleLoginRequestDTO dto
    ) {
        AuthenticationDTO result = authService.loginWithGoogle(dto);
        return ApiResponse.<AuthenticationDTO>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.LOGIN_SUCCESS)
                .data(result)
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(
            @RequestHeader("Authorization") String token
    ) throws Exception {
        String jwt = token.replace("Bearer ", "");
        authService.logout(jwt);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.LOGOUT_SUCCESS)
                .build();
    }

    @PostMapping("/refresh-token")
    public ApiResponse<TokenDTO> refreshToken(
            @RequestHeader("refresh_token") String refreshToken
    ) {
        TokenDTO tokenDTO = authService.refreshToken(refreshToken);
        return ApiResponse.<TokenDTO>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.TOKEN_REFRESH_SUCCESS)
                .data(tokenDTO)
                .build();
    }

    // =========================
    // ACCOUNT (REGISTER / OTP)
    // =========================

    @PostMapping("/register")
    public ApiResponse<Void> register(
            @Valid @RequestBody RegisterRequestDTO dto
    ) {
        accountService.register(dto);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.REGISTER_SUCCESS)
                .build();
    }

    @PostMapping("/verify-registration")
    public ApiResponse<Void> verifyRegistration(
            @RequestBody OtpVerificationDTO dto
    ) {
        accountService.verifyRegistrationByOtp(dto);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.VERIFY_REGISTRATION_SUCCESS)
                .build();
    }

    @PostMapping("/forgot-password")
    public ApiResponse<Void> forgotPassword(
            @RequestBody ForgotPasswordDTO dto
    ) {
        accountService.forgotPassword(dto);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.RESEND_OTP_SUCCESS)
                .build();
    }

    @PostMapping("/verify-otp")
    public ApiResponse<Void> verifyOtp(
            @RequestBody OtpVerificationDTO dto
    ) {
        accountService.verifyOtp(dto);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.VERIFY_SUCCESS)
                .build();
    }

    @PostMapping("/reset-password")
    public ApiResponse<Void> resetPassword(
            @Valid @RequestBody UpdatePasswordDTO dto
    ) {
        accountService.updatePassword(dto);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.RESET_PASSWORD_SUCCESS)
                .build();
    }
}
