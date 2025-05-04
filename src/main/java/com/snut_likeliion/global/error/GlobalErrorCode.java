package com.snut_likeliion.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GlobalErrorCode implements BaseError {

    BAD_REQUEST("BAD_REQUEST", "잘못된 요청입니다."),
    INVALID_AUTH_DATA("INVALID_AUTH_DATA", "인증 정보가 일치하지 않습니다."),
    UNAUTHORIZED("UNAUTHORIZED", "로그인이 필요한 서비스입니다."),
    INVALID_TOKEN("INVALID_TOKEN", "유효하지 않은 토큰입니다."),
    FORBIDDEN("FORBIDDEN", "권한이 없습니다."),
    NOT_FOUND("NOT_FOUND", "자원을 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR("SERVER_ERROR", "서버 오류입니다. 잠시 후 다시 시도해주세요.");

    private final String code;
    private final String message;
}