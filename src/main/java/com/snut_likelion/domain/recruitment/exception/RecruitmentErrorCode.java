package com.snut_likelion.domain.recruitment.exception;

import com.snut_likelion.global.error.BaseError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RecruitmentErrorCode implements BaseError {

    NOT_FOUND_RECRUITMENT("NOT_FOUND", "해당 모집 정보를 찾을 수 없습니다."),
    INVALID_DATE_RANGE("BAD_REQUEST", "모집 시작일은 종료일보다 늦을 수 없습니다."),
<<<<<<< HEAD
    ;
=======
    ALREADY_ENROLLED_EMAIL("BAD_REQUEST", "이미 등록된 이메일입니다." ),;
>>>>>>> 6de69ba85508f0cbec27e57958f0783643f34360

    private final String code;
    private final String message;
}
