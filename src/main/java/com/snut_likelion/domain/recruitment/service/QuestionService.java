package com.snut_likelion.domain.recruitment.service;

<<<<<<< HEAD
=======
import com.snut_likelion.domain.recruitment.dto.request.CreateQuestionRequest;
import com.snut_likelion.domain.recruitment.dto.request.UpdateQuestionRequest;
>>>>>>> 6de69ba85508f0cbec27e57958f0783643f34360
import com.snut_likelion.domain.recruitment.dto.response.QuestionResponse;
import com.snut_likelion.domain.recruitment.entity.DepartmentType;
import com.snut_likelion.domain.recruitment.entity.Question;
import com.snut_likelion.domain.recruitment.entity.Recruitment;
<<<<<<< HEAD
import com.snut_likelion.domain.recruitment.exception.RecruitmentErrorCode;
import com.snut_likelion.domain.recruitment.infra.QuestionFilter;
=======
import com.snut_likelion.domain.recruitment.exception.QuestionErrorCode;
import com.snut_likelion.domain.recruitment.exception.RecruitmentErrorCode;
import com.snut_likelion.domain.recruitment.infra.QuestionFilter;
import com.snut_likelion.domain.recruitment.infra.QuestionRepository;
>>>>>>> 6de69ba85508f0cbec27e57958f0783643f34360
import com.snut_likelion.domain.recruitment.infra.RecruitmentRepository;
import com.snut_likelion.domain.user.entity.Part;
import com.snut_likelion.global.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
<<<<<<< HEAD
=======
import org.springframework.transaction.annotation.Transactional;
>>>>>>> 6de69ba85508f0cbec27e57958f0783643f34360

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final RecruitmentRepository recruitmentRepository;
<<<<<<< HEAD
=======
    private final QuestionRepository questionRepository;
>>>>>>> 6de69ba85508f0cbec27e57958f0783643f34360
    private final QuestionFilter questionFilter;

    public List<QuestionResponse> getQuestions(Long recId, Part part, DepartmentType department) {
        Recruitment recruitment = recruitmentRepository.findWithQuestionsById(recId)
                .orElseThrow(() -> new NotFoundException(RecruitmentErrorCode.NOT_FOUND_RECRUITMENT));
        List<Question> questions = questionFilter.getRequiredQuestions(recruitment, part, department);
        return questions.stream()
                .map(QuestionResponse::from)
                .toList();
    }

<<<<<<< HEAD
=======
    @Transactional
    public void createQuestion(Long recId, CreateQuestionRequest req) {
        Recruitment recruitment = recruitmentRepository.findById(recId)
                .orElseThrow(() -> new NotFoundException(RecruitmentErrorCode.NOT_FOUND_RECRUITMENT));
        Question question = req.toEntity();
        recruitment.addQuestion(question);
        recruitmentRepository.save(recruitment);
    }

    @Transactional
    public void updateQuestion(Long questionId, UpdateQuestionRequest req) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException(QuestionErrorCode.NOT_FOUND_QUESTION));
        question.update(req.getText(), req.getQuestionType(), req.getPart(), req.getDepartmentType());
    }

    @Transactional
    public void removeQuestion(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException(QuestionErrorCode.NOT_FOUND_QUESTION));
        questionRepository.delete(question);
    }
>>>>>>> 6de69ba85508f0cbec27e57958f0783643f34360
}
