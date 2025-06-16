package com.snut_likelion.domain.auth.exception;

import com.snut_likelion.global.error.BaseError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements BaseError {

    NOT_FOUND_REFRESH_TOKEN("NOT_FOUND", "리프레시 토큰 정보를 찾을 수 없습니다."),
    INVALID_CERTIFICATION_TOKEN("INVALID_CERTIFICATION_TOKEN", "인증 코드가 유효하지 않거나 만료되었습니다.");

    private final String code;
    private final String message;
    }
