package com.snut_likelion.domain.recruitment.entity;

import com.snut_likelion.domain.user.entity.Part;
import com.snut_likelion.global.support.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends BaseEntity {

    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    @Enumerated(EnumType.STRING)
    private Part part;  // 파트 질문일 경우

    @Enumerated(EnumType.STRING)
    private DepartmentType departmentType; // 부서 질문일 경우

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruitment_id", referencedColumnName = "id")
    private Recruitment recruitment; // 해당 질문이 속한 모집 공고

    @Builder
    public Question(Long id, String text, QuestionType questionType, Part part, DepartmentType departmentType) {
        this.id = id;
        this.text = text;
        this.questionType = questionType;
        this.part = part;
        this.departmentType = departmentType;
    }

    public void update(String text, QuestionType questionType, Part part, DepartmentType departmentType) {
        if (StringUtils.hasText(text)) {
            this.text = text;
        }

        if (questionType != null) {
            this.questionType = questionType;
        }

        if (part != null) {
            this.part = part;
        }

        if (departmentType != null) {
            this.departmentType = departmentType;
        }
    }

    /**
     * === 연관관계 메서드 ===
     */
    public void setRecruitment(Recruitment recruitment) {
        this.recruitment = recruitment;
    }
}
