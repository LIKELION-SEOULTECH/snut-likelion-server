package com.snut_likeliion.domain.project.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateRetrospectionRequest {

    private String content;

    public UpdateRetrospectionRequest(String content) {
        this.content = content;
    }
}
