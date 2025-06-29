package com.snut_likelion.domain.recruitment.entity;

<<<<<<< HEAD
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum QuestionType {

    SHORT("단답형"),
    LONG("장문형"),
    RADIO_BUTTON("라디오 버튼형"),
    ;

    private final String description;
=======
public enum QuestionType {
    COMMON, // 공통 질문
    PART, // 파트 질문
    DEPARTMENT // 부서 질문
>>>>>>> 6de69ba85508f0cbec27e57958f0783643f34360
}
