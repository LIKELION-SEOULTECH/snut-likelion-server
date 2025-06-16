package com.snut_likelion.global.error.exception;

import com.snut_likelion.global.error.BaseError;
import org.springframework.http.HttpStatus;

public class ExistingResourceException extends SnutLikeLionException {
    public ExistingResourceException(BaseError error) {
        super(error, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    public ExistingResourceException(BaseError error, String message) {
        super(error, HttpStatus.UNPROCESSABLE_ENTITY, message);
    }
}
