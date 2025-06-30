package com.snut_likelion.global.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;

import static org.springframework.http.HttpMethod.*;

@Getter
@RequiredArgsConstructor
public enum PermitAllUrls {
    CSS("/css/**", GET),
    IMAGES("/images/**", GET),
    JS("/js/**", GET),
    FAVICON("/favicon.*", GET),
    ICON("/*/icon-*", GET),
    REGISTER("/api/v1/auth/register", POST),
    SEND_CERTIFICATION_CODE("/api/v1/auth/email/send", POST),
    CERTIFY_CODE("/api/v1/auth/email/certify", POST),
    FIND_PASSWORD("/api/v1/auth/password/find", POST),
    RESET_PASSWORD("/api/v1/auth/password/reset", PATCH),
    REFRESH("/api/v1/auth/refresh", GET),
    GET_IMAGE("/api/v1/images*", GET),
    GET_ALL_PROJECTS("/api/v1/projects", GET),
    GET_PROJECT("/api/v1/projects/{projectId}", GET),
    GET_PROJECT_RETROSPECTIONS("/api/v1/projects/{projectId}/retrospections", GET),
    GET_MEMBERS("/api/v1/members", GET),
    SEARCH_MEMBERS("/api/v1/members/search*", GET),
    GET_MEMBER_DETAILS("/api/v1/members/{memberId}", GET),
    GET_MEMBER_LION_INFO("/api/v1/members/{memberId}/lion-info", GET),
    GET_CURRENT_RECRUITMENT("/api/v1/recruitments", GET),
    GET_RECRUITMENT_QUESTIONS("/api/v1/recruitments/{recId}/questions", GET),
    SUBSCRIBE("/api/v1/subscriptions*", POST),
    GET_SAYINGS("/api/v1/sayings", GET),
    ;

    private final String url;
    private final HttpMethod method;
}
