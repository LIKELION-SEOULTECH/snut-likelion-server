package com.snut_likeliion.infra.file;

import com.snut_likeliion.global.error.BaseError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FileErrorCode implements BaseError {

    BAD_FILE_URL("BAD_REQUEST", "잘못된 파일 URL입니다."),
    FILE_NOT_FOUND("NOT_FOUND", "파일을 찾을 수 없습니다");

    private final String code;
    private final String message;
}
