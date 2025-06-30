package com.snut_likelion.domain.recruitment.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.snut_likelion.domain.recruitment.entity.Question;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuestionResponse {

    private Long id;
    private String text;
    private String questionTarget;
    private String questionType;
    private String part;
    private String departmentType;
    private int orderNum;
    private List<String> buttonList;

    @Builder
    public QuestionResponse(Long id, String text, String questionTarget, String questionType, String part, String departmentType, int orderNum, List<String> buttonList) {
        this.id = id;
        this.text = text;
        this.questionTarget = questionTarget;
        this.questionType = questionType;
        this.part = part;
        this.departmentType = departmentType;
        this.orderNum = orderNum;
        this.buttonList = buttonList;
    }

    public static QuestionResponse from(Question question) {
        return QuestionResponse.builder()
                .id(question.getId())
                .text(question.getText())
                .questionTarget(question.getQuestionTarget().getDescription())
                .questionType(question.getQuestionType().getDescription())
                .part(question.getPart() != null ? question.getPart().getDescription() : null)
                .departmentType(question.getDepartmentType() != null ? question.getDepartmentType().getDescription() : null)
                .orderNum(question.getOrderNum())
                .buttonList(question.getButtonList().isEmpty() ? null : question.getButtonList())
                .build();
    }
}
