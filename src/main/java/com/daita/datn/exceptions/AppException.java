package com.daita.datn.exceptions;

import com.daita.datn.enums.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AppException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String formattedMessage;
    private final HttpStatus httpStatus;

    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.formattedMessage = errorCode.getMessage();
        this.httpStatus = errorCode.getHttpStatus();
    }

    public AppException(ErrorCode errorCode, Object... args) {
        super(String.format(errorCode.getMessage(), args));
        this.errorCode = errorCode;
        this.formattedMessage = super.getMessage();
        this.httpStatus = errorCode.getHttpStatus();
    }
}
