package com.snut_likelion.domain.recruitment.dto.request;

import com.snut_likelion.domain.recruitment.entity.DepartmentType;
import com.snut_likelion.domain.user.entity.Part;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateApplicationRequest {

    @NotEmpty(message = "전공을 입력해주세요.")
    private String major;

    @NotEmpty(message = "학번을 입력해주세요.")
    private String studentId;

    @NotNull(message = "학년을 입력해주세요.")
    @Min(value = 1, message = "학년은 1 이상이어야 합니다.")
    private int grade;

    @NotNull(message = "재학 여부를 입력해주세요.")
    private Boolean inSchool;

    private List<ApplicationAnswerRequest> answers;

    @NotNull(message = "개인정보 수집 동의 여부를 입력해주세요.")
    private Boolean isPersonalInfoConsent;

    @NotNull(message = "포트폴리오 파일을 업로드해주세요.")
    private MultipartFile portfolio;

    @NotNull(message = "지원 파트를 선택해주세요.")
    private Part part;

    private DepartmentType departmentType;

    @Builder
    public UpdateApplicationRequest(String major, String studentId, int grade, Boolean inSchool, List<ApplicationAnswerRequest> answers, Boolean isPersonalInfoConsent, MultipartFile portfolio, Part part, DepartmentType departmentType) {
        this.major = major;
        this.studentId = studentId;
        this.grade = grade;
        this.inSchool = inSchool;
        this.answers = answers;
        this.isPersonalInfoConsent = isPersonalInfoConsent;
        this.portfolio = portfolio;
        this.part = part;
        this.departmentType = departmentType;
    }
}
