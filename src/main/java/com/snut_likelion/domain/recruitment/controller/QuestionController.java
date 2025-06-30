package com.snut_likelion.domain.recruitment.controller;

import com.snut_likelion.domain.recruitment.dto.response.QuestionResponse;
import com.snut_likelion.domain.recruitment.entity.DepartmentType;
import com.snut_likelion.domain.recruitment.service.QuestionService;
import com.snut_likelion.domain.user.entity.Part;
import com.snut_likelion.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
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
    ) {
        return ApiResponse.success(questionService.getQuestions(recId, part, department), "질문 조회 성공");
    }

}
