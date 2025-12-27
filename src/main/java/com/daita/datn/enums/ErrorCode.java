package com.daita.datn.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // === 1. Authentication & Authorization ===
    UNAUTHENTICATED("Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED("You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_CREDENTIALS("Email or password is incorrect", HttpStatus.UNAUTHORIZED),
    ACCOUNT_LOCKED("Your account is locked", HttpStatus.UNAUTHORIZED),
    ACCOUNT_DISABLED("Your account is inactive", HttpStatus.UNAUTHORIZED),
    UNVERIFIED_ACCOUNT("Account is not verified", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN("Token is invalid or expired", HttpStatus.UNAUTHORIZED),
    OTP_NOT_VERIFIED("OTP has not been verified", HttpStatus.UNAUTHORIZED),

    // === 2. Validation Errors ===
    VALIDATION_ERROR("Invalid input data", HttpStatus.BAD_REQUEST),
    INVALID_OTP( "Invalid or expired OTP", HttpStatus.BAD_REQUEST),

    // === 3. Business Logic Errors ===
    OPERATION_NOT_ALLOWED("Operation is not allowed", HttpStatus.NOT_ACCEPTABLE ),
    INSUFFICIENT_STOCK("Insufficient stock for product %s", HttpStatus.CONFLICT),
    ACCOUNT_HAS_ORDERS("Account has orders and cannot be deleted", HttpStatus.CONFLICT),
    CATEGORY_HAS_PRODUCTS("Please delete related products before deleting the category", HttpStatus.CONFLICT),
    BRAND_HAS_PRODUCTS("Please delete related products before deleting the brand", HttpStatus.CONFLICT),
    OTP_LIMIT_EXCEEDED("Exceeded %s OTP limit", HttpStatus.TOO_MANY_REQUESTS),
    REGISTRATION_EXPIRED("Registration session has expired. Please register again.", HttpStatus.GONE),

    // === 4. Resource / Data Not Found or Conflicts ===
    RESOURCE_NOT_FOUND("%s not found", HttpStatus.NOT_FOUND),
    RESOURCE_ALREADY_EXISTS("%s already exists", HttpStatus.CONFLICT),

    // === 5. External Services / Upload Errors ===
    UPLOAD_FAILED("Failed to upload file", HttpStatus.NOT_IMPLEMENTED),
    IMAGE_DELETE_FAILED("Failed to delete image", HttpStatus.NOT_IMPLEMENTED),
    INVALID_IMAGE_FORMAT("The image format you sent is invalid", HttpStatus.NOT_IMPLEMENTED),
    MAIL_SEND_FAILED("Failed to send email", HttpStatus.SERVICE_UNAVAILABLE),
    IMAGE_SIZE_EXCEEDED("Image size exceeds the limit", HttpStatus.NOT_IMPLEMENTED),
    FILE_READ_ERROR("Can not read file", HttpStatus.NOT_IMPLEMENTED),
    OTP_SEND_FAILED("Failed to send OTP", HttpStatus.SERVICE_UNAVAILABLE),
    EXTERNAL_SERVICE_ERROR("External service failed: %s", HttpStatus.BAD_GATEWAY),

    // === 9. System & Uncategorized ===
    NOT_IMPLEMENTED("Feature not implemented", HttpStatus.NOT_IMPLEMENTED),
    DATABASE_ERROR("Database error", HttpStatus.NOT_IMPLEMENTED),
    UNCATEGORIZED_EXCEPTION("Unexpected error occurred", HttpStatus.NOT_IMPLEMENTED),
    GENERATE_TOKEN_EXCEPTION("Failed to generate token", HttpStatus.NOT_IMPLEMENTED),
    SERVER_ERROR("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public int getHttpStatusCode() {
        return httpStatus.value();
    }
}
