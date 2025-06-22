package com.snut_likelion.domain.recruitment.dto.response;

import com.snut_likelion.domain.recruitment.entity.Answer;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApplicationAnswerResponse {

    private Long questionId;
    private String questionText;
    private String answer;

    @Builder
    public ApplicationAnswerResponse(Long questionId, String questionText, String answer) {
        this.questionId = questionId;
        this.questionText = questionText;
        this.answer = answer;
    }

    public static ApplicationAnswerResponse from(Answer answer) {
        return ApplicationAnswerResponse.builder()
                .questionId(answer.getQuestion().getId())
                .questionText(answer.getQuestion().getText())
                .answer(answer.getText())
                .build();
    }
}
