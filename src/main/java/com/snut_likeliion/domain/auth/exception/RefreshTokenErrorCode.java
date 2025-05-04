package com.snut_likeliion.domain.auth.exception;

import com.snut_likeliion.global.error.BaseError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RefreshTokenErrorCode implements BaseError {

    NOT_FOUND_REFRESH_TOKEN("NOT_FOUND", "리프레시 토큰 정보를 찾을 수 없습니다.");

    private final String code;
    private final String message;
}
