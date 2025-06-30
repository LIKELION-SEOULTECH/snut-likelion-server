package com.snut_likelion.domain.blog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class BlogException extends RuntimeException {

    private final BlogErrorCode errorCode;

    public BlogException(BlogErrorCode code, Throwable cause) {
        super(code.getMessage(), cause);
        this.errorCode = code;
    }

    public BlogException(BlogErrorCode code) {
        super(code.getMessage());
        this.errorCode = code;
    }

    public BlogErrorCode getErrorCode() { return errorCode; }
}