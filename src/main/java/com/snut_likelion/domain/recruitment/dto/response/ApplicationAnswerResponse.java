package com.snut_likelion.domain.recruitment.dto.response;

<<<<<<< HEAD
import com.fasterxml.jackson.annotation.JsonInclude;
import com.snut_likelion.domain.recruitment.entity.Answer;
import com.snut_likelion.domain.recruitment.entity.Question;
=======
import com.snut_likelion.domain.recruitment.entity.Answer;
>>>>>>> 6de69ba85508f0cbec27e57958f0783643f34360
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

<<<<<<< HEAD
import java.util.List;

=======
>>>>>>> 6de69ba85508f0cbec27e57958f0783643f34360
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApplicationAnswerResponse {

    private Long questionId;
    private String questionText;
    private String answer;
<<<<<<< HEAD
    private int order;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> buttonList;

    @Builder
    public ApplicationAnswerResponse(Long questionId, String questionText, String answer, int order, List<String> buttonList) {
        this.questionId = questionId;
        this.questionText = questionText;
        this.answer = answer;
        this.order = order;
        this.buttonList = buttonList;
    }

    public static ApplicationAnswerResponse from(Answer answer) {
        Question question = answer.getQuestion();

        return ApplicationAnswerResponse.builder()
                .questionId(question.getId())
                .questionText(question.getText())
                .answer(answer.getText())
                .order(question.getOrderNum())
                .buttonList(question.getButtonList().isEmpty() ? null : question.getButtonList())
                .build();
    }

=======

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
>>>>>>> 6de69ba85508f0cbec27e57958f0783643f34360
}
