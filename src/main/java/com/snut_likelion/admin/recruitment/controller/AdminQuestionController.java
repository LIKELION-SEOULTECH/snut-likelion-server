package com.snut_likelion.admin.recruitment.controller;

import com.snut_likelion.admin.recruitment.dto.request.UpdateQuestionsRequest;
import com.snut_likelion.admin.recruitment.service.AdminQuestionService;
import com.snut_likelion.domain.recruitment.dto.response.QuestionResponse;
import com.snut_likelion.global.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_MANAGER')")
public class AdminQuestionController {

    private final AdminQuestionService adminQuestionService;

    @GetMapping("/recruitments/{recId}/questions")
    public ApiResponse<List<QuestionResponse>> getQuestions(
            @PathVariable("recId") Long recId
    ) {
        return ApiResponse.success(
                adminQuestionService.getAllQuestions(recId),
                "질문 조회 성공"
        );
    }

    @PutMapping("/recruitments/{recId}/questions")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<Object> updateQuestions(
            @PathVariable("recId") Long recId,
            @RequestBody @Valid List<UpdateQuestionsRequest> req
    ) {
        adminQuestionService.updateQuestions(recId, req);
        return ApiResponse.success("질문 생성 성공");
    }

}
