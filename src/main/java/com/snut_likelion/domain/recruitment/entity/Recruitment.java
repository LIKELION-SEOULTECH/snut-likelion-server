package com.snut_likelion.domain.recruitment.entity;

import com.snut_likelion.domain.recruitment.exception.RecruitmentErrorCode;
import com.snut_likelion.global.error.exception.BadRequestException;
import com.snut_likelion.global.support.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recruitment extends BaseEntity {

    private int generation;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RecruitmentType recruitmentType;

    public LocalDateTime openDate;
    public LocalDateTime closeDate;

    @OneToMany(mappedBy = "recruitment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions;

    @OneToMany(mappedBy = "recruitment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applications;

    @Builder
    public Recruitment(Long id, int generation, RecruitmentType recruitmentType, LocalDateTime openDate, LocalDateTime closeDate) {
        this.id = id;
        this.generation = generation;
        this.recruitmentType = recruitmentType;
        this.openDate = openDate;
        this.closeDate = closeDate;
    }

    public void update(int generation, String recruitmentType, LocalDateTime openDate, LocalDateTime closeDate) {
        if (generation != 0) {
            this.generation = generation;
        }

        if (StringUtils.hasText(recruitmentType)) {
            this.recruitmentType = RecruitmentType.valueOf(recruitmentType);
        }

        if (openDate != null) {
            this.validateDates(openDate, this.closeDate);
            this.openDate = openDate;
        }

        if (closeDate != null) {
            this.validateDates(this.openDate, closeDate);
            this.closeDate = closeDate;
        }

    }

    private void validateDates(LocalDateTime openDate, LocalDateTime closeDate) {
        if (openDate.isAfter(closeDate)) {
            throw new BadRequestException(RecruitmentErrorCode.INVALID_DATE_RANGE);
        }
    }

    /**
     * === 연관관계 메서드 ===
     */
    public void addQuestion(Question question) {
        this.questions.add(question);
        question.setRecruitment(this);
    }

    public void addApplication(Application application) {
        this.applications.add(application);
        application.setRecruitment(this);
    }
}
