package com.snut_likeliion.global.error.exception;

import com.snut_likeliion.global.error.BaseError;
import org.springframework.http.HttpStatus;

public class UnsupportedMediaTypeException extends SnutLikeLionException {
    public UnsupportedMediaTypeException(BaseError error) {
        super(error, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    public UnsupportedMediaTypeException(BaseError error, String message) {
        super(error, HttpStatus.UNSUPPORTED_MEDIA_TYPE, message);
    }
}
