package com.snut_likelion.domain.recruitment.dto.response;

import com.snut_likelion.domain.recruitment.entity.Question;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionResponse {

    private Long id;
    private String text;
    private String questionType;
    private String part;
    private String departmentType;

    @Builder
    public QuestionResponse(Long id, String text, String questionType, String part, String departmentType) {
        this.id = id;
        this.text = text;
        this.questionType = questionType;
        this.part = part;
        this.departmentType = departmentType;
    }

    public static QuestionResponse from(Question question) {
        return QuestionResponse.builder()
                .id(question.getId())
                .text(question.getText())
                .questionType(question.getQuestionType().name())
                .part(question.getPart() != null ? question.getPart().name() : null)
                .departmentType(question.getDepartmentType() != null ? question.getDepartmentType().name() : null)
                .build();
    }
}
