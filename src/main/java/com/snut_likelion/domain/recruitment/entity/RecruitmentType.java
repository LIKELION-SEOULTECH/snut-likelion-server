package com.snut_likelion.domain.recruitment.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RecruitmentType {
    MEMBER("아기사자"), MANAGER("운영진");

    private final String description;
}
