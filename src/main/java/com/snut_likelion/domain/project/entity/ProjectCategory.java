package com.snut_likelion.domain.project.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProjectCategory {
    IDEATHON("아이디어톤"),
    HACKATHON("중앙 해커톤"),
    DEMO_DAY("데모데이"),
    LONG_TERM_PROJECT("장기 프로젝트"),
    ;

    private final String description;
}
