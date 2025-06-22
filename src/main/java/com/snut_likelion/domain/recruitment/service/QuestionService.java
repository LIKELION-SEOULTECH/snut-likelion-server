package com.snut_likelion.domain.recruitment.service;

import com.snut_likelion.domain.recruitment.dto.request.CreateQuestionRequest;
import com.snut_likelion.domain.recruitment.dto.request.UpdateQuestionRequest;
import com.snut_likelion.domain.recruitment.dto.response.QuestionResponse;
import com.snut_likelion.domain.recruitment.entity.DepartmentType;
import com.snut_likelion.domain.recruitment.entity.Question;
import com.snut_likelion.domain.recruitment.entity.Recruitment;
import com.snut_likelion.domain.recruitment.exception.QuestionErrorCode;
import com.snut_likelion.domain.recruitment.exception.RecruitmentErrorCode;
import com.snut_likelion.domain.recruitment.infra.QuestionFilter;
import com.snut_likelion.domain.recruitment.infra.QuestionRepository;
import com.snut_likelion.domain.recruitment.infra.RecruitmentRepository;
import com.snut_likelion.domain.user.entity.Part;
import com.snut_likelion.global.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final RecruitmentRepository recruitmentRepository;
    private final QuestionRepository questionRepository;
    private final QuestionFilter questionFilter;

    public List<QuestionResponse> getQuestions(Long recId, Part part, DepartmentType department) {
        Recruitment recruitment = recruitmentRepository.findWithQuestionsById(recId)
                .orElseThrow(() -> new NotFoundException(RecruitmentErrorCode.NOT_FOUND_RECRUITMENT));
        List<Question> questions = questionFilter.getRequiredQuestions(recruitment, part, department);
        return questions.stream()
                .map(QuestionResponse::from)
                .toList();
    }

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
}
