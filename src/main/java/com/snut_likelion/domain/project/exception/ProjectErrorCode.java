package com.snut_likelion.domain.project.exception;

import com.snut_likelion.global.error.BaseError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProjectErrorCode implements BaseError {

    UPDATE_BAD_REQUEST("BAD_REQUEST", "업데이트할 항목이 하나 이상 존재해야 합니다."),
    INVALID_FILE_FORMAT("INVALID_FILE_FORMAT", "이미지만 전달 가능합니다."),
    NOT_FOUND_PARTICIPANT("NOT_FOUND", "참여자 정보를 찾을 수 없습니다."),
    NOT_FOUND_PROJECT("NOT_FOUND", "프로젝트 정보를 찾을 수 없습니다."),
    NOT_FOUND_IMAGE("NOT_FOUND", "프로젝트 이미지를 찾을 수 없습니다."),
    NOT_FOUND_RETROSPECTION("NOT_FOUND", "프로젝트 회고를 찾을 수 없습니다."),
    NOT_FOUND_LION_INFO("NOT_FOUND", "해당 참여자의 라이온 정보를 찾을 수 없습니다."),
    MEMBER_IDS_NOT_PROVIDED("BAD_REQUEST", "참여자 ID가 제공되지 않았습니다."),
    ;

    private final String code;
    private final String message;
}
