package com.snut_likelion.domain.recruitment.entity;

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
}
