package com.snut_likelion.admin.recruitment.controller;

import com.snut_likelion.admin.recruitment.dto.request.CreateRecruitmentRequest;
import com.snut_likelion.admin.recruitment.dto.request.UpdateRecruitmentRequest;
import com.snut_likelion.admin.recruitment.service.AdminRecruitmentService;
import com.snut_likelion.global.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/recruitments")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_MANAGER')")
public class AdminRecruitmentController {

    private final AdminRecruitmentService adminRecruitmentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Object> createRecruitment(
            @RequestBody @Valid CreateRecruitmentRequest req
    ) {
        adminRecruitmentService.createRecruitment(req);
        return ApiResponse.success("모집 공고 생성 완료");
    }

    @PatchMapping("/{recId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateRecruitment(
            @PathVariable("recId") Long recId,
            @RequestBody @Valid UpdateRecruitmentRequest req
    ) {
        adminRecruitmentService.updateRecruitment(recId, req);
    }

    @DeleteMapping("/{recId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeRecruitment(
            @PathVariable("recId") Long recId
    ) {
        adminRecruitmentService.removeRecruitment(recId);
    }
}
