package com.snut_likelion.domain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ROLE_USER("아기 사자"),
    ROLE_MANAGER("운영진"),
    ROLE_ADMIN("대표");

    private final String description;
}
