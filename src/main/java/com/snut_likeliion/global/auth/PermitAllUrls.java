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
    REFRESH("/api/v1/auth/refresh", GET);

    private final String url;
    private final HttpMethod method;
}
