package com.snut_likelion.domain.recruitment.exception;

import com.snut_likelion.global.error.BaseError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum QuestionErrorCode implements BaseError {

    NOT_FOUND_QUESTION("NOT_FOUND", "질문을 찾을 수 없습니다."),
    ;

    private final String code;
    private final String message;
}
