package com.snut_likeliion.domain.auth.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FindPasswordRequest {

    private String name;
    private String email;

    @Builder
    public FindPasswordRequest(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
