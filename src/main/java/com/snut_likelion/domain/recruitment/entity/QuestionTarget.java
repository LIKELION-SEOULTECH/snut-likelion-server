package com.snut_likelion.domain.recruitment.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum QuestionTarget {
    COMMON("공통 질문"), // 공통 질문
    PART("파트 질문"), // 파트 질문
    DEPARTMENT("부서 질문"), // 부서 질문
    ;

    private final String description;
}
