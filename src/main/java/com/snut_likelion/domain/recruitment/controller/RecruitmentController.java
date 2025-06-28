package com.snut_likelion.domain.recruitment.controller;

import com.snut_likelion.domain.recruitment.dto.response.RecruitmentResponse;
import com.snut_likelion.domain.recruitment.entity.RecruitmentType;
import com.snut_likelion.domain.recruitment.service.RecruitmentService;
import com.snut_likelion.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/recruitments")
@RequiredArgsConstructor
public class RecruitmentController {

    private final RecruitmentService recruitmentService;

    @GetMapping
    public ApiResponse<RecruitmentResponse> getRecentRecruitmentInfo(
            @RequestParam("recruitmentType") RecruitmentType recruitmentType
    ) {
        return ApiResponse.success(
                recruitmentService.getRecentRecruitmentInfo(recruitmentType),
                "최근 모집 공고 조회 성공"
        );
    }

}
