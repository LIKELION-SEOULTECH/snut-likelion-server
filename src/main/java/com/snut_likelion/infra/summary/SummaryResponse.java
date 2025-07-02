package com.snut_likelion.infra.summary;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SummaryResponse {

    private String summary;

    public SummaryResponse(String summary) {
        this.summary = summary;
    }
}
