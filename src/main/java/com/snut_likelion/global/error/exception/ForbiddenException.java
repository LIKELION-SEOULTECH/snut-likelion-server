package com.snut_likelion.global.error.exception;

import com.snut_likelion.global.error.BaseError;
import org.springframework.http.HttpStatus;

public class ForbiddenException extends SnutLikeLionException {
    public ForbiddenException(BaseError error) {
        super(error, HttpStatus.FORBIDDEN);
    }

    public ForbiddenException(BaseError error, String message) {
        super(error, HttpStatus.FORBIDDEN, message);
    }
}
