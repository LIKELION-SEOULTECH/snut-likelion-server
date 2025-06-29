package com.snut_likelion.domain.recruitment.service;

import com.snut_likelion.domain.recruitment.dto.request.ApplicationAnswerRequest;
import com.snut_likelion.domain.recruitment.dto.request.CreateApplicationRequest;
import com.snut_likelion.domain.recruitment.dto.request.UpdateApplicationRequest;
import com.snut_likelion.domain.recruitment.entity.*;
import com.snut_likelion.domain.recruitment.exception.ApplicationErrorCode;
import com.snut_likelion.domain.recruitment.exception.RecruitmentErrorCode;
import com.snut_likelion.domain.recruitment.infra.ApplicationRepository;
import com.snut_likelion.domain.recruitment.infra.QuestionFilter;
import com.snut_likelion.domain.recruitment.infra.QuestionRepository;
import com.snut_likelion.domain.recruitment.infra.RecruitmentRepository;
<<<<<<< HEAD
import com.snut_likelion.domain.user.entity.Part;
=======
import com.snut_likelion.domain.user.entity.LionInfo;
import com.snut_likelion.domain.user.entity.Part;
import com.snut_likelion.domain.user.entity.Role;
>>>>>>> 6de69ba85508f0cbec27e57958f0783643f34360
import com.snut_likelion.domain.user.entity.User;
import com.snut_likelion.domain.user.exception.UserErrorCode;
import com.snut_likelion.domain.user.repository.UserRepository;
import com.snut_likelion.global.auth.model.UserInfo;
import com.snut_likelion.global.error.exception.BadRequestException;
import com.snut_likelion.global.error.exception.NotFoundException;
import com.snut_likelion.global.provider.FileProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationCommandServiceTest {

    @Mock
    ApplicationRepository applicationRepository;

    @Mock
    FileProvider fileProvider;

    @Mock
    RecruitmentRepository recruitmentRepository;

    @Mock
    QuestionRepository questionRepository;

    @Mock
    UserRepository userRepository;

    @Mock
<<<<<<< HEAD
=======
    NotificationService notificationService;

    @Mock
>>>>>>> 6de69ba85508f0cbec27e57958f0783643f34360
    QuestionFilter questionFilter;

    @InjectMocks
    ApplicationCommandService service;

    Long recId = 1L, userId = 1L, appId = 1L;
    Recruitment recruitment;
    User user;
    Question q1, q2, q3, q4, q5, q6;

    @BeforeEach
    void setup() {
        recruitment = Recruitment.builder()
                .id(recId)
                .generation(13)
                .recruitmentType(RecruitmentType.MEMBER)
                .openDate(LocalDateTime.of(2025, 6, 22, 0, 0))
                .closeDate(LocalDateTime.of(2025, 6, 29, 23, 59))
                .build();
        user = User.builder()
                .id(userId)
                .username("tester")
                .email("test@example.com")
                .build();

<<<<<<< HEAD
        q1 = this.createQuestion(1L, QuestionTarget.COMMON, null, null);
        q2 = this.createQuestion(2L, QuestionTarget.COMMON, null, null);
        q3 = this.createQuestion(3L, QuestionTarget.PART, Part.BACKEND, null);
        q4 = this.createQuestion(4L, QuestionTarget.PART, Part.BACKEND, null);
        q5 = this.createQuestion(5L, QuestionTarget.DEPARTMENT, null, DepartmentType.OPERATION);
        q6 = this.createQuestion(6L, QuestionTarget.DEPARTMENT, null, DepartmentType.OPERATION);

    }

    private Question createQuestion(Long id, QuestionTarget type, Part part, DepartmentType departmentType) {
        return Question.builder()
                .id(id)
                .text("q" + id)
                .questionTarget(type)
=======
        q1 = this.createQuestion(1L, QuestionType.COMMON, null, null);
        q2 = this.createQuestion(2L, QuestionType.COMMON, null, null);
        q3 = this.createQuestion(3L, QuestionType.PART, Part.BACKEND, null);
        q4 = this.createQuestion(4L, QuestionType.PART, Part.BACKEND, null);
        q5 = this.createQuestion(5L, QuestionType.DEPARTMENT, null, DepartmentType.OPERATION);
        q6 = this.createQuestion(6L, QuestionType.DEPARTMENT, null, DepartmentType.OPERATION);

    }

    private Question createQuestion(Long id, QuestionType type, Part part, DepartmentType departmentType) {
        return Question.builder()
                .id(id)
                .text("q" + id)
                .questionType(type)
>>>>>>> 6de69ba85508f0cbec27e57958f0783643f34360
                .part(part)
                .departmentType(departmentType)
                .build();
    }

    @Test
    void createApplication_draft() {
        ApplicationAnswerRequest ar1 = createAnsReq(1L);
        ApplicationAnswerRequest ar2 = createAnsReq(2L);
        ApplicationAnswerRequest ar3 = createAnsReq(3L);
        ApplicationAnswerRequest ar4 = createAnsReq(4L);

        CreateApplicationRequest req = CreateApplicationRequest.builder()
                .major("공학")
                .studentId("20201234")
                .grade(3)
                .inSchool(true)
                .isPersonalInfoConsent(true)
                .part(Part.BACKEND)
                .departmentType(null)
                .portfolio(new MockMultipartFile("file", "test.pdf", "application/pdf", new byte[]{1, 2, 3}))
                .answers(List.of(ar1, ar2, ar3, ar4))
                .build();

        when(recruitmentRepository.findById(recId)).thenReturn(Optional.of(recruitment));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(fileProvider.storeFile(any())).thenReturn("stored.pdf");
        when(questionRepository.findById(q1.getId())).thenReturn(Optional.of(q1));
        when(questionRepository.findById(q2.getId())).thenReturn(Optional.of(q2));
        when(questionRepository.findById(q3.getId())).thenReturn(Optional.of(q3));
        when(questionRepository.findById(q4.getId())).thenReturn(Optional.of(q4));

        // when
        service.createApplication(recId, userId, false, req);

        // then
        ArgumentCaptor<Application> captor = ArgumentCaptor.forClass(Application.class);
        verify(applicationRepository).save(captor.capture());
        Application saved = captor.getValue();

        assertAll(
                () -> assertThat(saved.getUser()).isEqualTo(user),
                () -> assertThat(saved.getMajor()).isEqualTo("공학"),
                () -> assertThat(saved.getInSchool()).isTrue(),
                () -> assertThat(saved.getStudentId()).isEqualTo("20201234"),
                () -> assertThat(saved.getGrade()).isEqualTo(3),
                () -> assertThat(saved.getIsPersonalInfoConsent()).isTrue(),
                () -> assertThat(saved.getPortfolioName()).isEqualTo("stored.pdf"),
                () -> assertThat(saved.getPart()).isEqualTo(Part.BACKEND),
                () -> assertThat(saved.getDepartmentType()).isNull(),
                () -> assertThat(saved.getStatus()).isEqualTo(ApplicationStatus.DRAFT),
                () -> assertThat(saved.getSubmittedAt()).isNotNull(),
                () -> assertThat(saved.getAnswers()).hasSize(4)
                        .extracting(Answer::getQuestion)
                        .containsExactlyInAnyOrder(q1, q2, q3, q4)
        );

    }

    @Test
    void createApplication_submit() {
        ApplicationAnswerRequest ar1 = createAnsReq(1L);
        ApplicationAnswerRequest ar2 = createAnsReq(2L);
        ApplicationAnswerRequest ar3 = createAnsReq(3L);
        ApplicationAnswerRequest ar4 = createAnsReq(4L);

        CreateApplicationRequest req = CreateApplicationRequest.builder()
                .major("공학")
                .studentId("20201234")
                .grade(3)
                .inSchool(true)
                .isPersonalInfoConsent(true)
                .part(Part.BACKEND)
                .departmentType(null)
                .portfolio(new MockMultipartFile("file", "test.pdf", "application/pdf", new byte[]{1, 2, 3}))
                .answers(List.of(ar1, ar2, ar3, ar4))
                .build();

        when(recruitmentRepository.findById(recId)).thenReturn(Optional.of(recruitment));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(fileProvider.storeFile(any())).thenReturn("stored.pdf");
        when(questionRepository.findById(q1.getId())).thenReturn(Optional.of(q1));
        when(questionRepository.findById(q2.getId())).thenReturn(Optional.of(q2));
        when(questionRepository.findById(q3.getId())).thenReturn(Optional.of(q3));
        when(questionRepository.findById(q4.getId())).thenReturn(Optional.of(q4));
        when(questionFilter.getRequiredQuestions(recruitment, req.getPart(), req.getDepartmentType())).thenReturn(List.of(q1, q2, q3, q4));
        // when
        service.createApplication(recId, userId, true, req);

        // then
        ArgumentCaptor<Application> captor = ArgumentCaptor.forClass(Application.class);
        verify(applicationRepository).save(captor.capture());
        Application saved = captor.getValue();

        assertAll(
                () -> assertThat(saved.getUser()).isEqualTo(user),
                () -> assertThat(saved.getMajor()).isEqualTo("공학"),
                () -> assertThat(saved.getInSchool()).isTrue(),
                () -> assertThat(saved.getStudentId()).isEqualTo("20201234"),
                () -> assertThat(saved.getGrade()).isEqualTo(3),
                () -> assertThat(saved.getIsPersonalInfoConsent()).isTrue(),
                () -> assertThat(saved.getPortfolioName()).isEqualTo("stored.pdf"),
                () -> assertThat(saved.getPart()).isEqualTo(Part.BACKEND),
                () -> assertThat(saved.getDepartmentType()).isNull(),
                () -> assertThat(saved.getStatus()).isEqualTo(ApplicationStatus.SUBMITTED),
                () -> assertThat(saved.getSubmittedAt()).isNotNull(),
                () -> assertThat(saved.getAnswers()).hasSize(4)
                        .extracting(Answer::getQuestion)
                        .containsExactlyInAnyOrder(q1, q2, q3, q4)
        );
    }

    @Test
    void createApplication_submit_manager() {
        recruitment = Recruitment.builder()
                .id(recId)
                .generation(13)
                .recruitmentType(RecruitmentType.MANAGER)
                .openDate(LocalDateTime.of(2025, 6, 22, 0, 0))
                .closeDate(LocalDateTime.of(2025, 6, 29, 23, 59))
                .build();

        ApplicationAnswerRequest ar1 = createAnsReq(1L);
        ApplicationAnswerRequest ar2 = createAnsReq(2L);
        ApplicationAnswerRequest ar3 = createAnsReq(3L);
        ApplicationAnswerRequest ar4 = createAnsReq(4L);
        ApplicationAnswerRequest ar5 = createAnsReq(5L);
        ApplicationAnswerRequest ar6 = createAnsReq(6L);

        CreateApplicationRequest req = CreateApplicationRequest.builder()
                .major("공학")
                .studentId("20201234")
                .grade(3)
                .inSchool(true)
                .isPersonalInfoConsent(true)
                .part(Part.FRONTEND)
                .departmentType(DepartmentType.OPERATION)
                .portfolio(new MockMultipartFile("file", "test.pdf", "application/pdf", new byte[]{1, 2, 3}))
                .answers(List.of(ar1, ar2, ar3, ar4, ar5, ar6))
                .build();

        when(recruitmentRepository.findById(recId)).thenReturn(Optional.of(recruitment));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(fileProvider.storeFile(any())).thenReturn("stored.pdf");
        when(questionRepository.findById(q1.getId())).thenReturn(Optional.of(q1));
        when(questionRepository.findById(q2.getId())).thenReturn(Optional.of(q2));
        when(questionRepository.findById(q3.getId())).thenReturn(Optional.of(q3));
        when(questionRepository.findById(q4.getId())).thenReturn(Optional.of(q4));
        when(questionRepository.findById(q5.getId())).thenReturn(Optional.of(q5));
        when(questionRepository.findById(q6.getId())).thenReturn(Optional.of(q6));
        when(questionFilter.getRequiredQuestions(recruitment, req.getPart(), req.getDepartmentType())).thenReturn(List.of(q1, q2, q3, q4, q5, q6));
        // when
        service.createApplication(recId, userId, true, req);

        // then
        ArgumentCaptor<Application> captor = ArgumentCaptor.forClass(Application.class);
        verify(applicationRepository).save(captor.capture());
        Application saved = captor.getValue();

        assertAll(
                () -> assertThat(saved.getUser()).isEqualTo(user),
                () -> assertThat(saved.getMajor()).isEqualTo("공학"),
                () -> assertThat(saved.getInSchool()).isTrue(),
                () -> assertThat(saved.getStudentId()).isEqualTo("20201234"),
                () -> assertThat(saved.getGrade()).isEqualTo(3),
                () -> assertThat(saved.getIsPersonalInfoConsent()).isTrue(),
                () -> assertThat(saved.getPortfolioName()).isEqualTo("stored.pdf"),
                () -> assertThat(saved.getPart()).isEqualTo(Part.FRONTEND),
                () -> assertThat(saved.getDepartmentType()).isEqualTo(DepartmentType.OPERATION),
                () -> assertThat(saved.getStatus()).isEqualTo(ApplicationStatus.SUBMITTED),
                () -> assertThat(saved.getSubmittedAt()).isNotNull(),
                () -> assertThat(saved.getAnswers()).hasSize(6)
                        .extracting(Answer::getQuestion)
                        .containsExactlyInAnyOrder(q1, q2, q3, q4, q5, q6)
        );
    }

    private ApplicationAnswerRequest createAnsReq(long questionId) {
        return ApplicationAnswerRequest.builder()
                .questionId(questionId)
                .answer("ans" + questionId)
                .build();
    }

    @Test
    void createApplication_invalidAnswerSet_throws() {
        // Given
        ApplicationAnswerRequest ar1 = createAnsReq(1L);
        ApplicationAnswerRequest ar2 = createAnsReq(2L);
        ApplicationAnswerRequest ar3 = createAnsReq(3L);

        CreateApplicationRequest req = CreateApplicationRequest.builder()
                .major("공학")
                .studentId("20201234")
                .grade(3)
                .inSchool(true)
                .isPersonalInfoConsent(true)
                .part(Part.BACKEND)
                .departmentType(null)
                .portfolio(new MockMultipartFile("file", "test.pdf", "application/pdf", new byte[]{1, 2, 3}))
                .answers(List.of(ar1, ar2, ar3))
                .build();

        when(recruitmentRepository.findById(recId)).thenReturn(Optional.of(recruitment));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(questionRepository.findById(q1.getId())).thenReturn(Optional.of(q1));
        when(questionRepository.findById(q2.getId())).thenReturn(Optional.of(q2));
        when(questionRepository.findById(q3.getId())).thenReturn(Optional.of(q3));
        when(questionFilter.getRequiredQuestions(recruitment, req.getPart(), req.getDepartmentType())).thenReturn(List.of(q1, q2, q3, q4));

        // When / Then
        assertThatThrownBy(() -> service.createApplication(recId, userId, true, req))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining(ApplicationErrorCode.INVALID_ANSWER_SET.getMessage());

    }

    @Test
    void createApplication_missingRecruitment_throws() {
        // Given
        when(recruitmentRepository.findById(recId)).thenReturn(Optional.empty());
        CreateApplicationRequest req = CreateApplicationRequest.builder()
                .major("x")
                .studentId("y")
                .grade(1)
                .inSchool(true)
                .isPersonalInfoConsent(true)
                .part(Part.AI)
                .departmentType(null)
                .portfolio(new MockMultipartFile("f", "f", "txt", new byte[0]))
                .answers(List.of())
                .build();

        // When / Then
        assertThatThrownBy(() -> service.createApplication(recId, userId, false, req))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(RecruitmentErrorCode.NOT_FOUND_RECRUITMENT.getMessage());
    }

    @Test
    void createApplication_missingUser_throws() {
        // Given
        ApplicationAnswerRequest ar1 = createAnsReq(1L);
        when(recruitmentRepository.findById(recId)).thenReturn(Optional.of(recruitment));
        when(questionRepository.findById(q1.getId())).thenReturn(Optional.of(q1));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        CreateApplicationRequest req = CreateApplicationRequest.builder()
                .major("x")
                .studentId("y")
                .grade(1)
                .inSchool(true)
                .isPersonalInfoConsent(true)
                .part(Part.AI)
                .departmentType(null)
                .portfolio(new MockMultipartFile("f", "f", "txt", new byte[0]))
                .answers(List.of(ar1))
                .build();

        // When / Then
        assertThatThrownBy(() -> service.createApplication(recId, userId, false, req))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(UserErrorCode.NOT_FOUND.getMessage());
    }

    @Test
    void updateApplication_draft() {
        int currentGeneration = 13;
        Long appId = 1L;

        Application application = Application.builder().id(appId).build();

        ApplicationAnswerRequest ar1 = createAnsReq(1L);
        ApplicationAnswerRequest ar2 = createAnsReq(2L);
        ApplicationAnswerRequest ar3 = createAnsReq(3L);
        ApplicationAnswerRequest ar4 = createAnsReq(4L);

        UpdateApplicationRequest req = UpdateApplicationRequest.builder()
                .major("공학")
                .studentId("20201234")
                .grade(3)
                .inSchool(true)
                .isPersonalInfoConsent(true)
                .part(Part.BACKEND)
                .departmentType(null)
                .portfolio(new MockMultipartFile("file", "test.pdf", "application/pdf", new byte[]{1, 2, 3}))
                .answers(List.of(ar1, ar2, ar3, ar4))
                .build();

        when(applicationRepository.findById(appId)).thenReturn(Optional.of(application));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(fileProvider.storeFile(any())).thenReturn("stored.pdf");
        when(questionRepository.findById(q1.getId())).thenReturn(Optional.of(q1));
        when(questionRepository.findById(q2.getId())).thenReturn(Optional.of(q2));
        when(questionRepository.findById(q3.getId())).thenReturn(Optional.of(q3));
        when(questionRepository.findById(q4.getId())).thenReturn(Optional.of(q4));

        UserInfo userInfo = UserInfo.from(user, currentGeneration);

        // when
        service.updateApplication(appId, userInfo, false, req);

        // then
        assertAll(
                () -> assertThat(application.getUser()).isEqualTo(user),
                () -> assertThat(application.getMajor()).isEqualTo("공학"),
                () -> assertThat(application.getInSchool()).isTrue(),
                () -> assertThat(application.getStudentId()).isEqualTo("20201234"),
                () -> assertThat(application.getGrade()).isEqualTo(3),
                () -> assertThat(application.getIsPersonalInfoConsent()).isTrue(),
                () -> assertThat(application.getPortfolioName()).isEqualTo("stored.pdf"),
                () -> assertThat(application.getPart()).isEqualTo(Part.BACKEND),
                () -> assertThat(application.getDepartmentType()).isNull(),
                () -> assertThat(application.getStatus()).isEqualTo(ApplicationStatus.DRAFT),
                () -> assertThat(application.getSubmittedAt()).isNotNull(),
                () -> assertThat(application.getAnswers()).hasSize(4)
                        .extracting(Answer::getQuestion)
                        .containsExactlyInAnyOrder(q1, q2, q3, q4)
        );
    }

    @Test
    void updateApplication_submit() {
        int currentGeneration = 13;
        Long appId = 1L;

        Application application = Application.builder().id(appId).build();
        application.setRecruitment(recruitment);

        ApplicationAnswerRequest ar1 = createAnsReq(1L);
        ApplicationAnswerRequest ar2 = createAnsReq(2L);
        ApplicationAnswerRequest ar3 = createAnsReq(3L);
        ApplicationAnswerRequest ar4 = createAnsReq(4L);

        UpdateApplicationRequest req = UpdateApplicationRequest.builder()
                .major("공학")
                .studentId("20201234")
                .grade(3)
                .inSchool(true)
                .isPersonalInfoConsent(true)
                .part(Part.BACKEND)
                .departmentType(null)
                .portfolio(new MockMultipartFile("file", "test.pdf", "application/pdf", new byte[]{1, 2, 3}))
                .answers(List.of(ar1, ar2, ar3, ar4))
                .build();

        when(applicationRepository.findById(appId)).thenReturn(Optional.of(application));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(fileProvider.storeFile(any())).thenReturn("stored.pdf");
        when(questionRepository.findById(q1.getId())).thenReturn(Optional.of(q1));
        when(questionRepository.findById(q2.getId())).thenReturn(Optional.of(q2));
        when(questionRepository.findById(q3.getId())).thenReturn(Optional.of(q3));
        when(questionRepository.findById(q4.getId())).thenReturn(Optional.of(q4));
        when(questionFilter.getRequiredQuestions(recruitment, req.getPart(), req.getDepartmentType())).thenReturn(List.of(q1, q2, q3, q4));

        UserInfo userInfo = UserInfo.from(user, currentGeneration);

        // when
        service.updateApplication(appId, userInfo, true, req);

        // then
        assertAll(
                () -> assertThat(application.getUser()).isEqualTo(user),
                () -> assertThat(application.getMajor()).isEqualTo("공학"),
                () -> assertThat(application.getInSchool()).isTrue(),
                () -> assertThat(application.getStudentId()).isEqualTo("20201234"),
                () -> assertThat(application.getGrade()).isEqualTo(3),
                () -> assertThat(application.getIsPersonalInfoConsent()).isTrue(),
                () -> assertThat(application.getPortfolioName()).isEqualTo("stored.pdf"),
                () -> assertThat(application.getPart()).isEqualTo(Part.BACKEND),
                () -> assertThat(application.getDepartmentType()).isNull(),
                () -> assertThat(application.getStatus()).isEqualTo(ApplicationStatus.SUBMITTED),
                () -> assertThat(application.getSubmittedAt()).isNotNull(),
                () -> assertThat(application.getAnswers()).hasSize(4)
                        .extracting(Answer::getQuestion)
                        .containsExactlyInAnyOrder(q1, q2, q3, q4)
        );
    }

    @Test
    void deleteApplication_success() {
        // given
        int currentGeneration = 13;
        UserInfo userInfo = UserInfo.from(user, currentGeneration);

        Application app = Application.builder().portfolioName("some.pdf").build();
        when(applicationRepository.findById(appId)).thenReturn(Optional.of(app));
        when(fileProvider.extractImageName("some.pdf")).thenReturn("stored.pdf");

        // when
        service.deleteApplication(appId, userInfo);

        // then
        verify(fileProvider).deleteFile("stored.pdf");
        verify(applicationRepository).delete(app);
    }

<<<<<<< HEAD
=======
    @Test
    void updateApplicationStatus_success_and_notify_INTERVIEW_SCHEDULED() {
        // given
        Application app = Application.builder()
                .id(1L)
                .status(ApplicationStatus.SUBMITTED)
                .part(Part.AI)
                .departmentType(null)
                .build();
        app.setUser(user);
        when(applicationRepository.findByIdWithUser(appId)).thenReturn(Optional.of(app));

        // when
        service.updateApplicationStatus(appId, ApplicationStatus.INTERVIEW_SCHEDULED);

        // then
        assertThat(app.getStatus()).isEqualTo(ApplicationStatus.INTERVIEW_SCHEDULED);
        verify(notificationService).sendNotification(user, ApplicationStatus.INTERVIEW_SCHEDULED, app);
    }

    @Test
    void updateApplicationStatus_success_and_notify_ACCEPTED() {
        // given
        Application app = Application.builder()
                .id(1L)
                .status(ApplicationStatus.INTERVIEW_SCHEDULED)
                .part(Part.AI)
                .departmentType(null)
                .build();
        app.setUser(user);
        when(applicationRepository.findByIdWithUser(appId)).thenReturn(Optional.of(app));

        // when
        service.updateApplicationStatus(appId, ApplicationStatus.ACCEPTED);

        // then
        LionInfo lionInfo = user.getLionInfos().get(0);
        assertAll(
                () -> assertThat(app.getStatus()).isEqualTo(ApplicationStatus.ACCEPTED),
                () -> assertThat(user.getLionInfos()).hasSize(1),
                () -> assertThat(lionInfo.getRole()).isEqualTo(Role.ROLE_USER),
                () -> assertThat(lionInfo.getPart()).isEqualTo(Part.AI),
                () -> verify(notificationService).sendNotification(user, ApplicationStatus.ACCEPTED, app)
        );
    }

    @Test
    void updateApplicationStatus_fromDraft_throws() {
        // given
        Application app = Application.builder()
                .status(ApplicationStatus.DRAFT).build();
        when(applicationRepository.findByIdWithUser(appId)).thenReturn(Optional.of(app));

        // when / then
        assertThatThrownBy(() -> service.updateApplicationStatus(appId, ApplicationStatus.DRAFT))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ApplicationErrorCode.DRAFT_APPLICATION_CANNOT_UPDATE_STATUS.getMessage());
    }

>>>>>>> 6de69ba85508f0cbec27e57958f0783643f34360
}