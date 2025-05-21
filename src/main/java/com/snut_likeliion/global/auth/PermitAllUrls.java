package com.snut_likeliion.global.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;

import static org.springframework.http.HttpMethod.*;

@Getter
@RequiredArgsConstructor
public enum PermitAllUrls {

    REGISTER("/api/v1/auth/register", POST),
    SEND_CERTIFICATION_CODE("/api/v1/auth/email/send", POST),
    CERTIFY_CODE("/api/v1/auth/email/certify", POST),
    FIND_PASSWORD("/api/v1/auth/password/find", POST),
    CHANGE_PASSWORD("/api/v1/auth/password/change", PATCH),
    REFRESH("/api/v1/auth/refresh", GET),
    GET_ALL_PROJECTS("/api/v1/projects", GET),
    GET_PROJECT("/api/v1/projects/{projectId}", GET),
    GET_PROJECT_RETROSPECTIONS("/api/v1/projects/{projectId}/retrospection", GET);

    private final String url;
    private final HttpMethod method;
}
