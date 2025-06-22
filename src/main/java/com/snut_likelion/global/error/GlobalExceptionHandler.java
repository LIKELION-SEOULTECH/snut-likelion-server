
package com.snut_likelion.global.error;

import com.snut_likelion.global.dto.ApiResponse;
import com.snut_likelion.global.error.exception.SnutLikeLionException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse> handleAccessDeniedException(AccessDeniedException e) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.fail(GlobalErrorCode.FORBIDDEN));
    }

    // DB 유니크·FK·NOT NULL 제약 위반 등
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.info("Data Integrity Violation Exception: {}", e.getMessage());
        Throwable root = NestedExceptionUtils.getRootCause(e);
        String detail = "데이터 무결성 제약 위반";

        // Hibernate 제약 위반인 경우 ConstraintViolationException 으로 캐스트해서 제약명 추출
        if (root instanceof ConstraintViolationException) {
            ConstraintViolationException cve = (ConstraintViolationException) root;
            String constraint = cve.getConstraintName();
            detail = String.format("제약조건 '%s' 위반", constraint);
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.fail(GlobalErrorCode.DATA_INTEGRITY_VIOLATION, detail));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse> handleMissingParameterException(MissingServletRequestParameterException e) {
        log.info("Missing Parameter Exception: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.fail(GlobalErrorCode.MISSING_PARAMETER));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.info("Method Argument Type Mismatch Exception: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.fail(GlobalErrorCode.BAD_REQUEST, "잘못된 요청 형식입니다."));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse> handle404(NoHandlerFoundException e) {
        log.info("Not Found Exception: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.fail(GlobalErrorCode.NOT_FOUND));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.info("Http Message Not Readable Exception: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.fail(GlobalErrorCode.BAD_REQUEST, "잘못된 요청 형식입니다."));
    }

    @ExceptionHandler(exception = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationException(MethodArgumentNotValidException e) {
        log.info("Validation Exception: {}", e.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.fail(GlobalErrorCode.BAD_REQUEST, this.createValidationErrorMessage(e)));
    }

    @ExceptionHandler(SnutLikeLionException.class)
    public ResponseEntity<ApiResponse> handleBusinessException(SnutLikeLionException e) {
        log.info("SNUT LikeLion Business Exception: {}", e.getMessage());

        return ResponseEntity
                .status(e.getHttpStatus())
                .body(ApiResponse.fail(e.getError()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleAllException(Exception e) {
        log.error("Unhandled Exception: ", e);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.fail(GlobalErrorCode.INTERNAL_SERVER_ERROR));
    }


    private String createValidationErrorMessage(MethodArgumentNotValidException e) {
        return e.getBindingResult().getAllErrors().stream()
                .map(error -> {
                    if (error instanceof FieldError) {
                        return ((FieldError) error).getField() + ": " + error.getDefaultMessage();
                    }
                    return error.getDefaultMessage();
                })
                .collect(Collectors.joining(", "));
    }
}
