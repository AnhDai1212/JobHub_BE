package com.daita.datn.common.constants;

public class Constant {

    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String TIMEZONE_VIETNAM = "Asia/Ho_Chi_Minh";
    public static final String[] PUBLIC_ENDPOINTS = {
            "/api/auth/login",
            "/api/auth/register",
            "/api/auth/forgot-password",
            "/api/auth/verify-otp",
            "/api/auth/reset-password",
            "/api/auth/verify-registration",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/api/v3/api-docs/**"
    };
    public static final String REFRESH_TOKEN_ENDPOINT = "/api/auth/refresh-token";
    public static final String REFRESH_TOKEN = "Refresh-Token";
    public static final String STATUS_UNAUTHORIZED = "UNAUTHORIZED";

    public static final String EMAIL_REGEX = "^[a-zA-Z0-9._]+@[a-zA-Z0-9.]+\\.[a-zA-Z]{2,}$";
    public static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,50}$";
    public static final String PHONE_REGEX = "^\\d{4,20}$";
}