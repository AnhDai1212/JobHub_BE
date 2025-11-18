package com.daita.datn.controllers;

import com.daita.datn.common.base.ApiResponse;
import com.daita.datn.common.constants.MessageConstant;
import com.daita.datn.models.dto.auth.*;
import com.daita.datn.services.AccountService;
import com.daita.datn.services.AuthService;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.text.ParseException;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AccountService accountService;
    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<?> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        accountService.login(loginRequestDTO);
        return ApiResponse.builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.LOGIN_SUCCESS)
                .data(accountService.login(loginRequestDTO))
                .build();
    }

    @PostMapping("/register")
    public ApiResponse register(@Valid @RequestBody RegisterRequestDTO requestDTO) {
        accountService.register(requestDTO);
        return ApiResponse.builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.REGISTER_SUCCESS)
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse logout(@RequestHeader(("Authorization")) String authHeader ) throws IOException, ParseException, JOSEException {
        String token = authHeader.replace("Bearer ", "");
        accountService.logout(token);
        return  ApiResponse.builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.LOGOUT_SUCCESS)
                .build();
    }

    @PostMapping("/refresh-token")
    public ApiResponse refreshToken(@RequestHeader(("refresh_token")) String refreshToken ) {
        TokenDTO tokenDTO = authService.refreshToken(refreshToken);
        return  ApiResponse.builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.TOKEN_REFRESH_SUCCESS)
                .data(tokenDTO)
                .build();
    }

    @PostMapping("/verify-registration")
    public ApiResponse verifyRegistration(@RequestBody OtpVerificationDTO otpVerificationDTO) {
        accountService.verifyRegistrationByOtp(otpVerificationDTO);
        return ApiResponse.builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.VERIFY_REGISTRATION_SUCCESS)
                .build();
    }

    @PostMapping("/forgot-password")
    public ApiResponse forgotPassword(@RequestBody ForgotPasswordDTO forgotPasswordDTO) {
        accountService.forgotPassword(forgotPasswordDTO);
        return ApiResponse.builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.RESEND_OTP_SUCCESS)
                .build();
    }

    @PostMapping("/verify-otp")
    public ApiResponse verifyOtp(@RequestBody OtpVerificationDTO otpVerificationDTO){
        accountService.verifyOtp(otpVerificationDTO);
        return ApiResponse.builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.VERIFY_SUCCESS)
                .build();
    }

    @PostMapping("/reset-password")
    public ApiResponse resetPassword(@RequestBody UpdatePasswordDTO updatePasswordDTO) {
        accountService.updatePassword(updatePasswordDTO);
        return ApiResponse.builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(MessageConstant.RESET_PASSWORD_SUCCESS)
                .build();
    }
}