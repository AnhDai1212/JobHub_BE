package com.daita.datn.common.constants;

public class MessageConstant {
    public static final String ERROR_NOT_FOUND_PREFIX_ENTITY = "Not found prefix entity";
    public static final String INVALID_EMAIL = "Email cannot be empty and must be valid.";
    public static final String INVALID_PASSWORD = "Password must be 8–50 chars with uppercase, lowercase and number.";
    public static final String INVALID_USER_NAME = "Username must be between 3-500 characters and contain only letters, numbers and spaces.";
    public static final String INVALID_PHONE = "Phone number must contain only digits and be 4–20 characters long.";
    public static final String INVALID_TAX_NUMBER = "Tax number must be 5–20 characters long and can contain digits and hyphens.";
    public static final String INVALID_MODEL_CODE = "Model code must contain only uppercase letters and numbers(8-20 characters)";
    public static final String INVALID_MODEL_NAME = "Model name must contain only letters, numbers, spaces, hyphens and dots(minimum 3 characters)";
    public static final String INVALID_WAREHOUSE_NAME = "The warehouse name must contain only letters and spaces, and be between 4 and 255 characters in length.";
    public static final String REFRESH_TOKEN_NOT_FOUND = "Refresh token [{}] does not exist";
    public static final String REFRESH_TOKEN_IS_REVOKED = "Refresh token [{}] is revoked";
    public static final String REFRESH_TOKEN_IS_EXPIRED = "Refresh token [{}] is expired";
    public static final String KEY_WORD_TOO_LONG = "Keyword is too long";
    public static final String PAGE_MIN = "Page index must be zero or greater.";
    public static final String PAGE_SIZE_MAX = "Page size must not exceed 100.";
    public static final String LOGIN_SUCCESS = "Login successful";
    public static final String LOGOUT_SUCCESS = "Logout successful";
    public static final String REGISTER_SUCCESS = "Email verified successfully. Please check your email for the OTP.";
    public static final String VERIFY_REGISTRATION_SUCCESS = "Your account has been successfully registered.";
    public static final String RESEND_OTP_SUCCESS = "Verification code has been sent to your email.";
    public static final String VERIFY_SUCCESS = "OTP verified successfully.";
    public static final String RESET_PASSWORD_SUCCESS = "Password has been reset successfully.";
    public static final String TOKEN_REFRESH_SUCCESS = "Token refreshed successfully";
    public static final String CREATED_USER_SUCCESS = "User created successfully";
    public static final String DELETED_USER_SUCCESS = "User deleted successfully.";
    public static final String FOUND_USER_SUCCESS = "User fetched successfully";
    public static final String UPDATE_USER_SUCCESS = "User updated successfully";
    public static final String USER_LIST_SUCCESS = "Userlist fetched successfully.";
    public static final String USER_SEARCH_FOR_ADD_MODEL_SUCCESS = "User search for add model successfully";
}
