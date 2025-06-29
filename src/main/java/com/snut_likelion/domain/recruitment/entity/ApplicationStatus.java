package com.snut_likelion.domain.recruitment.entity;

<<<<<<< HEAD
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
=======
public enum ApplicationStatus {
    DRAFT, // 임시 저장
    SUBMITTED, // 제출 완료
    INTERVIEW_SCHEDULED, // 면접 일정 확정
    ACCEPTED, // 합격 통보
    REJECTED // 불합격 통보
>>>>>>> 6de69ba85508f0cbec27e57958f0783643f34360
}
