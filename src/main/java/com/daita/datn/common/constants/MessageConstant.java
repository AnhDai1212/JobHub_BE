package com.daita.datn.common.constants;

public class MessageConstant {
    public static final String ERROR_NOT_FOUND_PREFIX_ENTITY = "Not found prefix entity";
    public static final String INVALID_EMAIL = "Email cannot be empty and must be valid.";
    public static final String INVALID_PASSWORD = "Password must be 8–50 chars with uppercase, lowercase, number, and special character.";
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
    public static final String FOUND_COMPANY_SUCCESS = "Company fetched successfully";
    public static final String UPDATE_USER_SUCCESS = "User updated successfully";
    public static final String USER_LIST_SUCCESS = "Userlist fetched successfully.";
    public static final String COMPANY_LIST_SUCCESS = "CompanyList fetched successfully.";
    public static final String USER_SEARCH_FOR_ADD_MODEL_SUCCESS = "User search for add model successfully";
    public static final String CREATE_COMPANY_SUCCESS = "Company created successfully";
    public static final String GET_COMPANY_SUCCESS = "Company fetched successfully";
    public static final String UPDATE_COMPANY_SUCCESS = "Company updated successfully";
    public static final String DELETE_COMPANY_SUCCESS = "Company deleted successfully";

    // Application
    public static final String APPLICATION_FETCH_SUCCESS = "Application fetched successfully";
    public static final String APPLICATION_STATUS_UPDATE_SUCCESS = "Application status updated successfully";
    public static final String APPLICATION_LIST_SUCCESS = "Application list fetched successfully";

    // Job
    public static final String JOB_CREATE_SUCCESS = "Job created successfully";
    public static final String JOB_FETCH_SUCCESS = "Job fetched successfully";
    public static final String JOB_LIST_SUCCESS = "Job list fetched successfully";
    public static final String JOB_UPDATE_SUCCESS = "Job updated successfully";
    public static final String JOB_STATUS_UPDATE_SUCCESS = "Job status updated successfully";
    public static final String JOB_DELETE_SUCCESS = "Job deleted successfully";
    public static final String JOB_JD_UPLOAD_SUCCESS = "Job JD uploaded successfully";

    // Job seeker
    public static final String JOB_SEEKER_FETCH_SUCCESS = "Job seeker fetched successfully";
    public static final String JOB_SEEKER_PROFILE_CREATE_SUCCESS = "Job seeker profile created successfully";
    public static final String JOB_SEEKER_PROFILE_UPDATE_SUCCESS = "Job seeker profile updated successfully";
    public static final String JOB_SEEKER_LIST_SUCCESS = "Job seekers fetched successfully";
    public static final String JOB_SEEKER_ACCOUNT_STATUS_UPDATE_SUCCESS = "Job seeker account status updated successfully";
    public static final String JOB_SEEKER_AVATAR_UPDATE_SUCCESS = "Job seeker avatar updated successfully";
    public static final String CV_PARSE_SUCCESS = "CV parsed successfully";
    public static final String PARSED_CV_SAVE_SUCCESS = "Parsed CV saved successfully";
    public static final String PARSED_CV_FETCH_SUCCESS = "Parsed CV fetched successfully";
    public static final String DASHBOARD_CHARTS_FETCH_SUCCESS = "Dashboard charts fetched successfully";
    public static final String CV_UPLOAD_SUCCESS = "CV uploaded successfully";
    public static final String CV_DELETE_SUCCESS = "CV deleted successfully";
    public static final String SKILL_LIST_SUCCESS = "Skills fetched successfully";
    public static final String SKILL_CREATE_SUCCESS = "Skill created successfully";
    public static final String SKILL_UPDATE_SUCCESS = "Skill updated successfully";
    public static final String SKILL_DELETE_SUCCESS = "Skill deleted successfully";
    public static final String APPLICATION_APPLY_SUCCESS = "Application submitted successfully";
    public static final String APPLICATION_WITHDRAW_SUCCESS = "Application withdrawn successfully";
    public static final String FAVORITE_ADD_SUCCESS = "Favorite added successfully";
    public static final String FAVORITE_REMOVE_SUCCESS = "Favorite removed successfully";
    public static final String FAVORITE_LIST_SUCCESS = "Favorites fetched successfully";
    public static final String CANDIDATE_CHAT_SUCCESS = "Candidate matched successfully";

    // Recruiter
    public static final String RECRUITER_FETCH_SUCCESS = "Recruiter fetched successfully";
    public static final String RECRUITER_LIST_SUCCESS = "Recruiters fetched successfully";
    public static final String RECRUITER_PENDING_LIST_SUCCESS = "Pending recruiters fetched successfully";
    public static final String RECRUITER_STATUS_UPDATE_SUCCESS = "Recruiter status updated successfully";
    public static final String RECRUITER_DOCUMENT_LIST_SUCCESS = "Recruiter documents fetched successfully";
    public static final String RECRUITER_ACCOUNT_STATUS_UPDATE_SUCCESS = "Recruiter account status updated successfully";
    public static final String RECRUITER_AVATAR_UPDATE_SUCCESS = "Recruiter avatar updated successfully";

    // Recruiter consultation/documents
    public static final String CONSULTATION_SAVE_SUCCESS = "Consultation saved successfully";
    public static final String DOCUMENT_UPLOAD_SUCCESS = "Document uploaded successfully";
    public static final String DOCUMENT_LIST_SUCCESS = "Documents fetched successfully";

    // Company misc
    public static final String COMPANY_INFO_UPDATE_SUCCESS = "Company info updated successfully";
    public static final String COMPANY_AVATAR_UPDATE_SUCCESS = "Avatar updated successfully";
}
