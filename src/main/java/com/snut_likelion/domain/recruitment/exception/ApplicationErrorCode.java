package com.snut_likelion.domain.recruitment.exception;

import com.snut_likelion.global.error.BaseError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApplicationErrorCode implements BaseError {

    NOT_FOUND_APPLICATION("NOT_FOUND", "지원서를 찾을 수 없습니다."),
    INVALID_STATUS_CHANGE("BAD_REQUEST", "임시 저장 혹은 제출 상태로 변경할 수 없습니다."),
    DRAFT_APPLICATION_CANNOT_UPDATE_STATUS("BAD_REQUEST", "임시 저장 상태의 지원서는 상태를 변경할 수 없습니다."),
    MANAGER_APPLICATION_MUST_HAVE_DEPARTMENT_TYPE("BAD_REQUEST", "운영진 지원서는 지원 부서가 반드시 있어야 합니다."),
    EMPTY_ANSWERS("BAD_REQUEST", "응답 내용들을 입력해주세요."),
<<<<<<< HEAD
    INVALID_ANSWER_SET("BAD_REQUEST", "지원하신 유형·파트·부서에 따른 질문에 모두 답변해 주세요."),
    ALREADY_SUBMITTED("BAD_REQUEST", "이미 제출된 지원서입니다."),
    ;
=======
    INVALID_ANSWER_SET("BAD_REQUEST", "지원하신 유형·파트·부서에 따른 질문에 모두 답변해 주세요.");
>>>>>>> 6de69ba85508f0cbec27e57958f0783643f34360

    private final String code;
    private final String message;
}
