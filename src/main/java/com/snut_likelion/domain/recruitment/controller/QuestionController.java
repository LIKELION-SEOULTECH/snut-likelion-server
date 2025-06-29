package com.snut_likelion.domain.recruitment.controller;

<<<<<<< HEAD
=======
import com.snut_likelion.domain.recruitment.dto.request.CreateQuestionRequest;
import com.snut_likelion.domain.recruitment.dto.request.UpdateQuestionRequest;
>>>>>>> 6de69ba85508f0cbec27e57958f0783643f34360
import com.snut_likelion.domain.recruitment.dto.response.QuestionResponse;
import com.snut_likelion.domain.recruitment.entity.DepartmentType;
import com.snut_likelion.domain.recruitment.service.QuestionService;
import com.snut_likelion.domain.user.entity.Part;
import com.snut_likelion.global.dto.ApiResponse;
<<<<<<< HEAD
import lombok.RequiredArgsConstructor;
=======
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
>>>>>>> 6de69ba85508f0cbec27e57958f0783643f34360
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("/recruitments/{recId}/questions")
    public ApiResponse<List<QuestionResponse>> getQuestions(
            @PathVariable("recId") Long recId,
            @RequestParam(value = "part", required = false) Part part,
            @RequestParam(value = "department", required = false) DepartmentType department
<<<<<<< HEAD
    ) {
        return ApiResponse.success(questionService.getQuestions(recId, part, department), "질문 조회 성공");
    }

=======
            ) {
        return ApiResponse.success(questionService.getQuestions(recId, part, department), "질문 조회 성공");
    }

    @PostMapping("/recruitments/{recId}/questions")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ApiResponse<Object> createQuestion(
            @PathVariable("recId") Long recId,
            @RequestBody @Valid CreateQuestionRequest req
    ) {
        questionService.createQuestion(recId, req);
        return ApiResponse.success("질문 생성 성공");
    }

    @PatchMapping("/questions/{questionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public void updateQuestion(
            @PathVariable("questionId") Long questionId,
            @RequestBody @Valid UpdateQuestionRequest req
    ) {
        questionService.updateQuestion(questionId, req);
    }

    @DeleteMapping("/questions/{questionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public void removeQuestion(
            @PathVariable("questionId") Long questionId
    ) {
        questionService.removeQuestion(questionId);
    }
>>>>>>> 6de69ba85508f0cbec27e57958f0783643f34360
}
