package com.snut_likeliion.global.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Getter
@RequiredArgsConstructor
public enum PermitAllUrls {

    REGISTER("/api/v1/auth/register", POST),
    SEND_CERTIFICATION_CODE("/api/v1/auth/email/send", POST),
    CERTIFY_CODE("/api/v1/auth/email/certify", POST),
    REFRESH("/api/v1/auth/refresh", GET);

    private final String url;
    private final HttpMethod method;
}
