package com.daita.datn.common.constants;

import java.util.Set;

public class Constant {

    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String TIMEZONE_VIETNAM = "Asia/Ho_Chi_Minh";
    public static final String[] PUBLIC_ENDPOINTS = {
            "/api/auth/login",
            "/api/auth/google-login",
            "/api/auth/register",
            "/api/auth/forgot-password",
            "/api/auth/verify-otp",
            "/api/auth/reset-password",
            "/api/auth/verify-registration",
            "/google-login-test.html",
            "/api/job-seekers/*",
            "/api/jobs/search",
            "/api/jobs/*",
            "/api/companies/*/jobs",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/api/v3/api-docs/**"
    };
    public static final String REFRESH_TOKEN_ENDPOINT = "/api/auth/refresh-token";
    public static final String REFRESH_TOKEN = "Refresh-Token";
    public static final String STATUS_UNAUTHORIZED = "UNAUTHORIZED";

    public static final String EMAIL_REGEX = "^[a-zA-Z0-9._]+@[a-zA-Z0-9.]+\\.[a-zA-Z]{2,}$";
    public static final String PASSWORD_REGEX =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,50}$";
    public static final String PHONE_REGEX = "^\\d{4,20}$";
    public static final String DEFAULT_COMPANY_AVATAR = "https://res.cloudinary.com/duxkk3hzk/image/upload/v1765478346/creative-logo-design-real-estate-company-vector-illustration_1253202-15082_ka99iq.avif";
    public static final Set<String> COMPANY_SEARCH_FIELDS =
            Set.of("companyName");

    public static final Set<String> COMPANY_SORT_FIELDS =
            Set.of("companyName", "companyId");

    public static final Set<String> JOB_SEARCH_FIELDS =
            Set.of("title", "location", "status");

    public static final Set<String> JOB_SORT_FIELDS =
            Set.of("createAt", "deadline", "title", "status");

    public static final Set<String> APPLICATION_SORT_FIELDS =
            Set.of("appliedAt", "status");

    public static final Set<String> FAVORITE_SORT_FIELDS =
            Set.of("favoriteId");

    public static final Set<String> JOB_SEEKER_SEARCH_FIELDS =
            Set.of("fullName", "account.email", "phone", "address", "bio");

    public static final Set<String> JOB_SEEKER_SORT_FIELDS =
            Set.of("createAt", "fullName", "account.email");

    public static final Set<String> RECRUITER_SEARCH_FIELDS =
            Set.of("account.email", "company.companyName", "phone", "position");

    public static final Set<String> RECRUITER_SORT_FIELDS =
            Set.of("createAt", "account.email", "company.companyName", "status");

    public static final Set<String> JOB_SEEKER_FETCH_RELATIONS =
            Set.of("account");

    public static final Set<String> RECRUITER_FETCH_RELATIONS =
            Set.of("account", "company");

    // File upload constraints (reuse across services)
    public static final long MAX_UPLOAD_FILE_SIZE_BYTES = 10 * 1024 * 1024; // 10MB
    public static final Set<String> ALLOWED_DOC_CONTENT_TYPES = Set.of(
            "application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "image/png",
            "image/jpeg"
    );
}
