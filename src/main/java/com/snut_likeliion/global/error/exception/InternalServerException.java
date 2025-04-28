package com.snut_likeliion.global.error.exception;

import com.snut_likeliion.global.error.BaseError;
import org.springframework.http.HttpStatus;

public class InternalServerException extends SnutLikeLionException {
    public InternalServerException(BaseError error) {
        super(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public InternalServerException(BaseError error, String message) {
        super(error, HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}
