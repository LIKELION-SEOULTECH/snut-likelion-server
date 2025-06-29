package com.snut_likelion.domain.recruitment.exception;

import com.snut_likelion.global.error.BaseError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum QuestionErrorCode implements BaseError {

    NOT_FOUND_QUESTION("NOT_FOUND", "질문을 찾을 수 없습니다."),
<<<<<<< HEAD
    INVALID_QUESTION_INCLUDE("BAD_REQUEST", "아기사자 모집 질문에는 운영진 질문을 포함할 수 없습니다.");
=======
    ;
>>>>>>> 6de69ba85508f0cbec27e57958f0783643f34360

    private final String code;
    private final String message;
}
