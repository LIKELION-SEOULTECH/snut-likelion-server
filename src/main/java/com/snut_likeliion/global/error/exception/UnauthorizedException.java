package com.snut_likeliion.global.error.exception;

import com.snut_likeliion.global.error.BaseError;
import org.springframework.http.HttpStatus;

public class UnauthorizedException extends SnutLikeLionException {
    public UnauthorizedException(BaseError error) {
        super(error, HttpStatus.UNAUTHORIZED);
    }

    public UnauthorizedException(BaseError error, String message) {
        super(error, HttpStatus.UNAUTHORIZED, message);
    }
}
