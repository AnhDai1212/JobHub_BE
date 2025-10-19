package com.daita.datn.exceptions.handlers;

import com.daita.datn.common.base.ExceptionResponse;
import com.daita.datn.common.constants.Constant;
import com.daita.datn.enums.ErrorCode;
import com.daita.datn.exceptions.AppException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGenericException(Exception exception, HttpServletRequest request) {
        return buildResponseEntity(ErrorCode.UNCATEGORIZED_EXCEPTION, request.getRequestURI(), ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ExceptionResponse> handleAppException(AppException exception, HttpServletRequest request) {
        return buildResponseEntity(exception.getErrorCode(), request.getRequestURI(), exception.getFormattedMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponse> handleAccessDenied(AccessDeniedException exception, HttpServletRequest request) {
        return buildResponseEntity(ErrorCode.UNAUTHORIZED, request.getRequestURI(), ErrorCode.UNAUTHORIZED.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ExceptionResponse> handleValidationExceptions(Exception ex, HttpServletRequest request) {
        if (ex instanceof MethodArgumentNotValidException validationEx) {
            Map<String, String> messages = new HashMap<>();
            for (FieldError fieldError : validationEx.getBindingResult().getFieldErrors()) {
                messages.putIfAbsent(fieldError.getField(), fieldError.getDefaultMessage());
            }
            return buildResponseEntity(ErrorCode.VALIDATION_ERROR, request.getRequestURI(), messages);
        } else {
            return buildResponseEntity(ErrorCode.VALIDATION_ERROR, request.getRequestURI(), ErrorCode.VALIDATION_ERROR.getMessage());
        }
    }

    @ExceptionHandler({MaxUploadSizeExceededException.class, MultipartException.class})
    public ResponseEntity<ExceptionResponse> handleMaxUploadFileException(Exception exception, HttpServletRequest request){
        return buildResponseEntity(ErrorCode.IMAGE_SIZE_EXCEEDED, request.getRequestURI(),ErrorCode.IMAGE_SIZE_EXCEEDED.getMessage());
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ExceptionResponse> handleIOException(IOException exception, HttpServletRequest request){
        return buildResponseEntity(ErrorCode.FILE_READ_ERROR, request.getRequestURI(), ErrorCode.FILE_READ_ERROR.getMessage());
    }

    private ResponseEntity<ExceptionResponse> buildResponseEntity(ErrorCode errorCode, String path, Object message) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(buildErrorResponse(errorCode, path, message));
    }

    private ExceptionResponse buildErrorResponse(ErrorCode errorCode, String path, Object message) {
        return ExceptionResponse.builder()
                .timestamp(LocalDateTime.now(ZoneId.of(Constant.TIMEZONE_VIETNAM)))
                .path(path)
                .code(errorCode.getHttpStatusCode())
                .status(errorCode.name())
                .message(message)
                .build();
    }
}