package com.snut_likelion.domain.recruitment.service;

import com.snut_likelion.domain.recruitment.dto.response.ApplicationAnswerResponse;
import com.snut_likelion.domain.recruitment.dto.response.ApplicationDetailsResponse;
import com.snut_likelion.domain.recruitment.entity.Answer;
import com.snut_likelion.domain.recruitment.entity.Application;
import com.snut_likelion.domain.recruitment.entity.Question;
<<<<<<< HEAD
import com.snut_likelion.domain.recruitment.entity.QuestionTarget;
import com.snut_likelion.domain.recruitment.exception.ApplicationErrorCode;
=======
import com.snut_likelion.domain.recruitment.entity.QuestionType;
import com.snut_likelion.domain.recruitment.exception.ApplicationErrorCode;
import com.snut_likelion.domain.recruitment.infra.ApplicationQueryRepository;
>>>>>>> 6de69ba85508f0cbec27e57958f0783643f34360
import com.snut_likelion.domain.recruitment.infra.ApplicationRepository;
import com.snut_likelion.domain.user.entity.Part;
import com.snut_likelion.domain.user.entity.User;
import com.snut_likelion.global.error.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
<<<<<<< HEAD
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
=======
>>>>>>> 6de69ba85508f0cbec27e57958f0783643f34360
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationQueryServiceTest {

    @Mock
    private ApplicationRepository applicationRepository;

<<<<<<< HEAD
=======
    @Mock
    private ApplicationQueryRepository applicationQueryRepository;

>>>>>>> 6de69ba85508f0cbec27e57958f0783643f34360
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
                .build();

<<<<<<< HEAD
        Question q1 = this.createQuestion(1L, QuestionTarget.COMMON, null);
        Question q2 = this.createQuestion(2L, QuestionTarget.COMMON, null);
        Question q3 = this.createQuestion(3L, QuestionTarget.PART, Part.PLANNING);
        Question q4 = this.createQuestion(4L, QuestionTarget.PART, Part.PLANNING);
=======
        Question q1 = this.createQuestion(1L, QuestionType.COMMON, null);
        Question q2 = this.createQuestion(2L, QuestionType.COMMON, null);
        Question q3 = this.createQuestion(3L, QuestionType.PART, Part.PLANNING);
        Question q4 = this.createQuestion(4L, QuestionType.PART, Part.PLANNING);
>>>>>>> 6de69ba85508f0cbec27e57958f0783643f34360

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
//        Application app2 = this.createApplication(2L, Part.DESIGN, LocalDateTime.of(2025, 6, 22, 11, 0));
//        Application app3 = this.createApplication(3L, Part.FRONTEND, LocalDateTime.of(2025, 6, 22, 12, 0));
//        Application app4 = this.createApplication(4L, Part.BACKEND, LocalDateTime.of(2025, 6, 22, 13, 0));
//        Application app5 = this.createApplication(5L, Part.AI, LocalDateTime.of(2025, 6, 22, 14, 0));

<<<<<<< HEAD
        when(applicationRepository.findMyApplication(eq(userId), anyInt()))
=======
        when(applicationRepository.findMyApplication(userId))
>>>>>>> 6de69ba85508f0cbec27e57958f0783643f34360
                .thenReturn(Optional.of(app1));

        // When
        ApplicationDetailsResponse response = applicationQueryService.getMyApplication(userId);

        // Then
        assertAll(
                () -> assertThat(response.getId()).isEqualTo(app1.getId()),
                () -> assertThat(response.getUsername()).isEqualTo(me.getUsername()),
                () -> assertThat(response.getMajor()).isEqualTo(app1.getMajor()),
                () -> assertThat(response.getInSchool()).isTrue(),
                () -> assertThat(response.getStudentId()).isEqualTo(String.valueOf(20000000 + app1.getId())),
                () -> assertThat(response.getGrade()).isEqualTo(app1.getGrade()),
                () -> assertThat(response.getIsPersonalInfoConsent()).isTrue(),
                () -> assertThat(response.getPortfolioName()).isEqualTo(app1.getPortfolioName()),
                () -> assertThat(response.getPart()).isEqualTo(app1.getPart().name()),
                () -> assertThat(response.getSubmittedAt()).isEqualTo(submittedAt),
                () -> assertThat(response.getDepartmentType()).isNull(),
                () -> assertThat(response.getStatus()).isEqualTo(app1.getStatus().name()),
                () -> assertThat(response.getAnswers()).hasSize(4)
                        .extracting(ApplicationAnswerResponse::getQuestionId, ApplicationAnswerResponse::getQuestionText, ApplicationAnswerResponse::getAnswer)
                        .containsExactly(
                                tuple(1L, "q1", "ans1"),
                                tuple(2L, "q2", "ans2"),
                                tuple(3L, "q3", "ans3"),
                                tuple(4L, "q4", "ans4")
                        )
        );
    }

    @Test
    void getMyApplication_whenNotFound_throwsNotFound() {
        // Given
        Long userId = 1L;
<<<<<<< HEAD
        when(applicationRepository.findMyApplication(eq(userId), anyInt()))
=======
        when(applicationRepository.findMyApplication(userId))
>>>>>>> 6de69ba85508f0cbec27e57958f0783643f34360
                .thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> applicationQueryService.getMyApplication(userId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ApplicationErrorCode.NOT_FOUND_APPLICATION.getMessage());

    }

    private Answer createAnswer(long id, Question q, Application app, String ans) {
        return Answer.builder()
                .id(id)
                .question(q)
                .application(app)
                .text(ans)
                .build();
    }

<<<<<<< HEAD
    private Question createQuestion(Long id, QuestionTarget type, Part part) {
        return Question.builder()
                .id(id)
                .text("q" + id)
                .questionTarget(type)
=======
    private Question createQuestion(Long id, QuestionType type, Part part) {
        return Question.builder()
                .id(id)
                .text("q" + id)
                .questionType(type)
>>>>>>> 6de69ba85508f0cbec27e57958f0783643f34360
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