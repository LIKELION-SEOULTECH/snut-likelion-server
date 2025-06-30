package com.snut_likelion.domain.recruitment.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApplicationStatus {
    DRAFT("임시 저장"),
    SUBMITTED("제출"),
    PAPER_PASS("서류 합격"),
    FINAL_PASS("최종 합격"),
    FAILED("불합격"),
    ;

    private final String description;
}
