package com.snut_likelion.infra.summary;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SummaryRequest {

    private String text;

    public SummaryRequest(String text) {
        this.text = text;
    }
}
