package com.snut_likelion.global.error.exception;

import com.snut_likelion.global.error.BaseError;
import org.springframework.http.HttpStatus;

public class BadRequestException extends SnutLikeLionException {
    public BadRequestException(BaseError error) {
        super(error, HttpStatus.BAD_REQUEST);
    }

    public BadRequestException(BaseError error, String message) {
        super(error, HttpStatus.BAD_REQUEST, message);
    }
}
