package com.snut_likelion.domain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Part {
    PLANNING("기획"),
    DESIGN("디자인"),
    FRONTEND("프론트엔드"),
    BACKEND("백엔드"),
    AI("AI");

    private final String description;
}
