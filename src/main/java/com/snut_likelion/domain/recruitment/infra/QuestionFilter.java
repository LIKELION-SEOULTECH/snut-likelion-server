package com.snut_likelion.domain.recruitment.infra;

import com.snut_likelion.domain.recruitment.entity.DepartmentType;
import com.snut_likelion.domain.recruitment.entity.Question;
<<<<<<< HEAD
import com.snut_likelion.domain.recruitment.entity.QuestionTarget;
=======
import com.snut_likelion.domain.recruitment.entity.QuestionType;
>>>>>>> 6de69ba85508f0cbec27e57958f0783643f34360
import com.snut_likelion.domain.recruitment.entity.Recruitment;
import com.snut_likelion.domain.user.entity.Part;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class QuestionFilter {

<<<<<<< HEAD
    public List<Question> getRequiredQuestions(Recruitment recruitment, Part part, DepartmentType department) {
        List<Question> allQuestions = recruitment.getQuestions();
        return allQuestions.stream()
                .filter(q -> {
                    // 공통 질문은 무조건
                    if (q.getQuestionTarget() == QuestionTarget.COMMON) {
=======
    private final QuestionRepository questionRepository;

    public List<Question> getRequiredQuestions(Recruitment recruitment, Part part, DepartmentType department) {
        List<Question> allQuestions = questionRepository.findAllByRecruitmentId(recruitment.getId());
        return allQuestions.stream()
                .filter(q -> {
                    // 공통 질문은 무조건
                    if (q.getQuestionType() == QuestionType.COMMON) {
>>>>>>> 6de69ba85508f0cbec27e57958f0783643f34360
                        return true;
                    }

                    switch (recruitment.getRecruitmentType()) {
                        // 회원 모집인 경우: PART 질문만, 그 중에서도 req.getPart 와 같은 것
                        case MEMBER -> {
<<<<<<< HEAD
                            return q.getQuestionTarget() == QuestionTarget.PART
=======
                            return q.getQuestionType() == QuestionType.PART
>>>>>>> 6de69ba85508f0cbec27e57958f0783643f34360
                                    && q.getPart() == part;
                        }
                        // 운영진 모집인 경우: PART + DEPARTMENT 둘 다
                        case MANAGER -> {
                            // PART 질문 중, req.getPart 와 같은 것
<<<<<<< HEAD
                            if (q.getQuestionTarget() == QuestionTarget.PART) {
                                return q.getPart() == part;
                            }
                            // DEPARTMENT 질문 중, req.getDepartmentType 와 같은 것
                            if (q.getQuestionTarget() == QuestionTarget.DEPARTMENT) {
=======
                            if (q.getQuestionType() == QuestionType.PART) {
                                return q.getPart() == part;
                            }
                            // DEPARTMENT 질문 중, req.getDepartmentType 와 같은 것
                            if (q.getQuestionType() == QuestionType.DEPARTMENT) {
>>>>>>> 6de69ba85508f0cbec27e57958f0783643f34360
                                return q.getDepartmentType() == department;
                            }
                        }
                    }

                    return false;
                })
                .toList();
    }
}
