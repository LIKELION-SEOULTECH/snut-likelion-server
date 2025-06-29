package com.snut_likelion.domain.recruitment.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DepartmentType {
    OPERATION("운영부"),
    ACADEMIC("학술부"),
    MARKETING("홍보부");

    private final String description;
}
