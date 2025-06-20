package com.snut_likelion.domain.blog.exception;

import com.snut_likelion.global.error.BaseError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BlogErrorCode implements BaseError {
    BLOG_FORBIDDEN("공식 행사는 관리자만 작성할 수 있습니다."),
    POST_NOT_FOUND("게시글을 찾을 수 없습니다."),
    DELETE_FORBIDDEN("게시글 삭제 권한이 없습니다.");

    private final String message;

    @Override
    public String getCode() {
        return this.name();
    }
}
