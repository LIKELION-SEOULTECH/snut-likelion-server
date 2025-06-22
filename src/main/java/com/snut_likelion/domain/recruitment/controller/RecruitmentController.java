package com.snut_likelion.domain.recruitment.controller;

import com.snut_likelion.domain.recruitment.dto.request.CreateRecruitmentRequest;
import com.snut_likelion.domain.recruitment.dto.request.UpdateRecruitmentRequest;
import com.snut_likelion.domain.recruitment.dto.response.RecruitmentResponse;
import com.snut_likelion.domain.recruitment.entity.RecruitmentType;
import com.snut_likelion.domain.recruitment.service.RecruitmentService;
import com.snut_likelion.global.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Object> createRecruitment(
            @RequestBody @Valid CreateRecruitmentRequest req
    ) {
        recruitmentService.createRecruitment(req);
        return ApiResponse.success("모집 공고 생성 완료");
    }

    @PatchMapping("/{recId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateRecruitment(
            @PathVariable("recId") Long recId,
            @RequestBody @Valid UpdateRecruitmentRequest req
    ) {
        recruitmentService.updateRecruitment(recId, req);
    }

    @DeleteMapping("/{recId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateRecruitment(
            @PathVariable("recId") Long recId
    ) {
        recruitmentService.removeRecruitment(recId);
    }
}
