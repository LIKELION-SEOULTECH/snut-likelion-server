package com.snut_likeliion.domain.project.exception;

import com.snut_likeliion.global.error.BaseError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProjectErrorCode implements BaseError {

    INVALID_FILE_FORMAT("INVALID_FILE_FORMAT", "이미지만 전달 가능합니다."),
    NOT_FOUND_PARTICIPANT("NOT_FOUND", "참여자 정보를 찾을 수 없습니다."),
    NOT_FOUND_PROJECT("NOT_FOUND", "프로젝트 정보를 찾을 수 없습니다."),
    NOT_FOUND_IMAGE("NOT_FOUND", "프로젝트 이미지를 찾을 수 없습니다."),
    NOT_FOUND_RETROSPECTION("NOT_FOUND", "프로젝트 회고를 찾을 수 없습니다.");

    private final String code;
    private final String message;
}
