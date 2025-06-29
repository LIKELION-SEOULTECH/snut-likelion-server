package com.snut_likelion.domain.recruitment.entity;

import com.snut_likelion.domain.user.entity.Part;
import com.snut_likelion.global.support.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
<<<<<<< HEAD

import java.util.ArrayList;
import java.util.List;
=======
import org.springframework.util.StringUtils;
>>>>>>> 6de69ba85508f0cbec27e57958f0783643f34360

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
<<<<<<< HEAD
//@Table(
//        name = "question",
//        uniqueConstraints = {
//                @UniqueConstraint(
//                        name = "UK_QUESTION_ORDER_RECRUITMENT",
//                        columnNames = {"orderNum", "recruitment_id"}
//                )
//        }
//)
=======
>>>>>>> 6de69ba85508f0cbec27e57958f0783643f34360
public class Question extends BaseEntity {

    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
<<<<<<< HEAD
    private QuestionTarget questionTarget;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
=======
>>>>>>> 6de69ba85508f0cbec27e57958f0783643f34360
    private QuestionType questionType;

    @Enumerated(EnumType.STRING)
    private Part part;  // 파트 질문일 경우

    @Enumerated(EnumType.STRING)
    private DepartmentType departmentType; // 부서 질문일 경우

<<<<<<< HEAD
    @Column(nullable = false)
    private int orderNum;

    private String buttonList;

=======
>>>>>>> 6de69ba85508f0cbec27e57958f0783643f34360
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruitment_id", referencedColumnName = "id")
    private Recruitment recruitment; // 해당 질문이 속한 모집 공고

    @Builder
<<<<<<< HEAD
    public Question(Long id, String text, QuestionTarget questionTarget, QuestionType questionType, Part part, DepartmentType departmentType, int orderNum, String buttonList) {
        this.id = id;
        this.text = text;
        this.questionTarget = questionTarget;
        this.questionType = questionType;
        this.part = part;
        this.departmentType = departmentType;
        this.orderNum = orderNum;
        this.buttonList = buttonList;
    }

    public List<String> getButtonList() {
        if (buttonList == null || buttonList.isEmpty()) {
            return new ArrayList<>();
        }

        return List.of(buttonList.split(", "));
=======
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
>>>>>>> 6de69ba85508f0cbec27e57958f0783643f34360
    }

    /**
     * === 연관관계 메서드 ===
     */
    public void setRecruitment(Recruitment recruitment) {
        this.recruitment = recruitment;
    }
}
