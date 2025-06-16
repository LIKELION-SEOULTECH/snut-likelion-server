package com.snut_likelion.global.error.exception;

import com.snut_likelion.global.error.BaseError;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class SnutLikeLionException extends RuntimeException {

    private final BaseError error;
    private final HttpStatus httpStatus;

    protected SnutLikeLionException(BaseError error, HttpStatus httpStatus) {
        super(error.getMessage());
        this.error = error;
        this.httpStatus = httpStatus;
    }

    protected SnutLikeLionException(BaseError error, HttpStatus httpStatus, String message) {
        super(message);
        this.error = error;
        this.httpStatus = httpStatus;
    }
}
