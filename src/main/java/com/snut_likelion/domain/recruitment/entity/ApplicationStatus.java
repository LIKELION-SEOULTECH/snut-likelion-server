package com.snut_likelion.domain.recruitment.entity;

public enum ApplicationStatus {
    DRAFT, // 임시 저장
    SUBMITTED, // 제출 완료
    INTERVIEW_SCHEDULED, // 면접 일정 확정
    ACCEPTED, // 합격 통보
    REJECTED // 불합격 통보
}
