package com.snut_likelion.domain.recruitment.dto.request;

import com.snut_likelion.domain.recruitment.entity.Answer;
import com.snut_likelion.domain.recruitment.entity.Application;
import com.snut_likelion.domain.recruitment.entity.Question;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApplicationAnswerRequest {

    private Long questionId;
    private String answer;

    @Builder
    public ApplicationAnswerRequest(Long questionId, String answer) {
        this.questionId = questionId;
        this.answer = answer;
    }

    public Answer toEntity(Question question, Application application) {
        return Answer.builder()
                .question(question)
                .application(application)
                .text(answer)
                .build();
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
