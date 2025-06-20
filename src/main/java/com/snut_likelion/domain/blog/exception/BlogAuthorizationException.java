package com.snut_likelion.domain.blog.exception;

import com.snut_likelion.global.error.exception.ForbiddenException;

public class BlogAuthorizationException extends ForbiddenException {

    public BlogAuthorizationException(BlogErrorCode errorCode) {
        super(errorCode);
    }

    // 공식 행사 작성 권한 없음
    public static BlogAuthorizationException notAdminForOfficial() {
        return new BlogAuthorizationException(BlogErrorCode.BLOG_FORBIDDEN);
    }

    // 삭제 권한 없음
    public static BlogAuthorizationException noDeletePermission() {
        return new BlogAuthorizationException(BlogErrorCode.DELETE_FORBIDDEN);
    }
}
