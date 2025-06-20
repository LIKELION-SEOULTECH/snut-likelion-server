package com.snut_likelion.global.error.exception;

import com.snut_likelion.global.error.BaseError;
import org.springframework.http.HttpStatus;

public class NotAcceptableException extends SnutLikeLionException {

    protected NotAcceptableException(BaseError error) {
        super(error, HttpStatus.NOT_ACCEPTABLE);
    }

    protected NotAcceptableException(BaseError error, String message) {
        super(error, HttpStatus.NOT_ACCEPTABLE, message);
    }
}
