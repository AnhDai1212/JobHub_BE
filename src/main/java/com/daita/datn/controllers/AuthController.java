package com.daita.datn.controllers;

import com.daita.datn.common.base.ApiResponse;
import com.daita.datn.common.constants.MessageConstant;
import com.daita.datn.models.dto.auth.LoginRequestDTO;
import com.daita.datn.models.dto.auth.OtpVerificationDTO;
import com.daita.datn.models.dto.auth.RegisterRequestDTO;
import com.daita.datn.models.dto.auth.TokenDTO;
import com.daita.datn.services.AccountService;
import com.daita.datn.services.AuthService;
import com.nimbusds.jose.JOSEException;
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
    public ApiResponse login(@RequestBody LoginRequestDTO loginRequestDTO) {
        return ApiResponse.builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.name())
                .data(accountService.login(loginRequestDTO))
                .build();
    }

    @PostMapping("/register")
    public ApiResponse register(@RequestBody RegisterRequestDTO requestDTO) {
        accountService.register(requestDTO);
        return ApiResponse.builder()
                .code(HttpStatus.OK.value())
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse logout(@RequestHeader(("Authorization")) String authHeader ) throws IOException, ParseException, JOSEException {
        String token = authHeader.replace("Bearer ", "");
        accountService.logout(token);
        return  ApiResponse.builder()
                .code(HttpStatus.OK.value())
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
}