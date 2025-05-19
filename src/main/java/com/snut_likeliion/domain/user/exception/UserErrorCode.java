package com.snut_likeliion.domain.user.exception;

import com.snut_likeliion.global.error.BaseError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements BaseError {

    NOT_FOUND("NOT_FOUND", "유저 정보를 찾을 수 없습니다."),
    EXISTING_USER("ALREADY_EXISTING_USER", "이미 존재하는 유저입니다.");

    private final String code;
    private final String message;
}
