package com.snut_likeliion.global.error.exception;

import com.snut_likeliion.global.error.BaseError;
import org.springframework.http.HttpStatus;

public class NotFoundException extends SnutLikeLionException {
    public NotFoundException(BaseError error) {
        super(error, HttpStatus.NOT_FOUND);
    }

    public NotFoundException(BaseError error, String message) {
        super(error, HttpStatus.NOT_FOUND, message);
    }
}
