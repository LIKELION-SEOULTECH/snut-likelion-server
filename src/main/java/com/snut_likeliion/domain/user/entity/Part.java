package com.snut_likeliion.domain.user.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Part {
    PLANNING("기획"),
    DESIGN("디자인"),
    FRONTEND("프론트엔드"),
    BACKEND("백엔드"),
    AI("AI");

    private final String name;
}
