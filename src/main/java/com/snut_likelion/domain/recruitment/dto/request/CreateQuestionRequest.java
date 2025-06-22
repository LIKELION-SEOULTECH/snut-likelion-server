package com.snut_likelion.domain.recruitment.dto.request;

import com.snut_likelion.domain.recruitment.entity.DepartmentType;
import com.snut_likelion.domain.recruitment.entity.Question;
import com.snut_likelion.domain.recruitment.entity.QuestionType;
import com.snut_likelion.domain.user.entity.Part;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateQuestionRequest {

    @NotEmpty(message = "질문 내용은 비어있을 수 없습니다.")
    private String text;

    @NotNull(message = "질문 유형은 필수입니다.")
    private QuestionType questionType;

    private Part part;

    private DepartmentType departmentType;

    @Builder
    public CreateQuestionRequest(String text, QuestionType questionType, Part part, DepartmentType departmentType) {
        this.text = text;
        this.questionType = questionType;
        this.part = part;
        this.departmentType = departmentType;
    }

    @AssertTrue
    public boolean isValid() {
        // 질문 유형이 PART인 경우, part가 null이 아니어야 함
        if (questionType == QuestionType.PART) {
            return part != null;
        }
        // 질문 유형이 DEPARTMENT인 경우, departmentType이 null이 아니어야 함
        if (questionType == QuestionType.DEPARTMENT) {
            return departmentType != null;
        }
        // COMMON 질문 유형은 추가 조건 없음
        return true;
    }

    public Question toEntity() {
        return Question.builder()
                .text(text)
                .questionType(questionType)
                .part(part)
                .departmentType(departmentType)
                .build();
    }
}
