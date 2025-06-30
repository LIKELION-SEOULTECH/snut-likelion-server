package com.snut_likelion.domain.recruitment.service;

import com.snut_likelion.domain.recruitment.dto.response.ApplicationAnswerResponse;
import com.snut_likelion.domain.recruitment.dto.response.ApplicationDetailsResponse;
import com.snut_likelion.domain.recruitment.entity.Answer;
import com.snut_likelion.domain.recruitment.entity.Application;
import com.snut_likelion.domain.recruitment.entity.Question;
import com.snut_likelion.domain.recruitment.entity.QuestionTarget;
import com.snut_likelion.domain.recruitment.infra.ApplicationRepository;
import com.snut_likelion.domain.user.entity.Part;
import com.snut_likelion.domain.user.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationQueryServiceTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @InjectMocks
    private ApplicationQueryService applicationQueryService;


    @Test
    void getMyApplication() {
        // Given
        Long userId = 1L;
        User me = User.builder()
                .id(userId)
                .username("user1")
                .email("test@test.com")
                .phoneNumber("01000000000")
                .build();

        Question q1 = this.createQuestion(1L, QuestionTarget.COMMON, null);
        Question q2 = this.createQuestion(2L, QuestionTarget.COMMON, null);
        Question q3 = this.createQuestion(3L, QuestionTarget.PART, Part.PLANNING);
        Question q4 = this.createQuestion(4L, QuestionTarget.PART, Part.PLANNING);

        LocalDateTime submittedAt = LocalDateTime.of(2025, 6, 22, 10, 0);
        Application app1 = this.createApplication(1L, Part.PLANNING, submittedAt);
        app1.setUser(me);
        Answer ans1 = this.createAnswer(1L, q1, app1, "ans1");
        Answer ans2 = this.createAnswer(2L, q2, app1, "ans2");
        Answer ans3 = this.createAnswer(3L, q3, app1, "ans3");
        Answer ans4 = this.createAnswer(4L, q4, app1, "ans4");
        app1.addAnswer(ans1);
        app1.addAnswer(ans2);
        app1.addAnswer(ans3);
        app1.addAnswer(ans4);

        when(applicationRepository.findMyApplication(eq(userId), anyInt()))
                .thenReturn(List.of(app1));

        // When
        List<ApplicationDetailsResponse> response = applicationQueryService.getMyApplication(userId);

        // Then
        ApplicationDetailsResponse myApp = response.get(0);
        assertAll(
                () -> assertThat(response.size()).isEqualTo(1),
                () -> assertThat(myApp.getId()).isEqualTo(app1.getId()),
                () -> assertThat(myApp.getUsername()).isEqualTo(me.getUsername()),
                () -> assertThat(myApp.getPhoneNumber()).isEqualTo(me.getPhoneNumber()),
                () -> assertThat(myApp.getMajor()).isEqualTo(app1.getMajor()),
                () -> assertThat(myApp.getInSchool()).isTrue(),
                () -> assertThat(myApp.getStudentId()).isEqualTo(String.valueOf(20000000 + app1.getId())),
                () -> assertThat(myApp.getGrade()).isEqualTo(app1.getGrade()),
                () -> assertThat(myApp.getIsPersonalInfoConsent()).isTrue(),
                () -> assertThat(myApp.getPortfolioName()).isEqualTo(app1.getPortfolioName()),
                () -> assertThat(myApp.getPart()).isEqualTo(app1.getPart().name()),
                () -> assertThat(myApp.getSubmittedAt()).isEqualTo(submittedAt),
                () -> assertThat(myApp.getDepartmentType()).isNull(),
                () -> assertThat(myApp.getStatus()).isEqualTo(app1.getStatus().name()),
                () -> assertThat(myApp.getAnswers()).hasSize(4)
                        .extracting(ApplicationAnswerResponse::getQuestionId, ApplicationAnswerResponse::getQuestionText, ApplicationAnswerResponse::getAnswer)
                        .containsExactly(
                                tuple(1L, "q1", "ans1"),
                                tuple(2L, "q2", "ans2"),
                                tuple(3L, "q3", "ans3"),
                                tuple(4L, "q4", "ans4")
                        )
        );
    }

    private Answer createAnswer(long id, Question q, Application app, String ans) {
        return Answer.builder()
                .id(id)
                .question(q)
                .application(app)
                .text(ans)
                .build();
    }

    private Question createQuestion(Long id, QuestionTarget type, Part part) {
        return Question.builder()
                .id(id)
                .text("q" + id)
                .questionTarget(type)
                .part(part)
                .build();
    }

    private Application createApplication(Long id,
                                          Part part,
                                          LocalDateTime submittedAt) {
        return Application.builder()
                .id(id)
                .major("컴퓨터공학과")
                .inSchool(true)
                .studentId(String.valueOf(20000000 + id))
                .grade(3)
                .isPersonalInfoConsent(true)
                .portfolioName("portfolio.pdf")
                .part(part)
                .submittedAt(submittedAt)
                .build();


    }
}