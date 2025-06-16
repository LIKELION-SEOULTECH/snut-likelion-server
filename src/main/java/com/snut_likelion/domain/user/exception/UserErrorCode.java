package com.snut_likelion.domain.user.exception;

import com.snut_likelion.global.error.BaseError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements BaseError {

    NOT_FOUND("NOT_FOUND", "유저 정보를 찾을 수 없습니다."),
    EXISTING_USER("ALREADY_EXISTING_USER", "이미 존재하는 유저입니다."),
    INVALID_PROFILE_IMAGE_FORMAT("INVALID_FORMAT", "이미지만 전달 가능합니다."),
    NOT_FOUND_LION_INFO("NOT_FOUND_LION_INFO", "해당 기수의 유저 정보를 찾을 수 없습니다.");

    private final String code;
    private final String message;
}
