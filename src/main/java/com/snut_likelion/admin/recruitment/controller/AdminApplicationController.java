package com.snut_likelion.admin.recruitment.controller;

import com.snut_likelion.admin.recruitment.dto.request.ApplicationListStatus;
import com.snut_likelion.admin.recruitment.dto.request.ChangeApplicationStatusParameter;
import com.snut_likelion.admin.recruitment.dto.request.ChangeApplicationStatusRequest;
import com.snut_likelion.admin.recruitment.dto.response.ApplicationPageResponse;
import com.snut_likelion.admin.recruitment.service.AdminApplicationService;
import com.snut_likelion.domain.recruitment.dto.response.ApplicationDetailsResponse;
import com.snut_likelion.domain.recruitment.dto.response.ApplicationResponse;
import com.snut_likelion.domain.user.entity.Part;
import com.snut_likelion.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ROLE_MANAGER')")
public class AdminApplicationController {

    private final AdminApplicationService adminApplicationService;

    @GetMapping("/recruitments/{recId}/applications")
    public ApiResponse<ApplicationPageResponse> getApplicationsByRecruitmentId(
            @PathVariable("recId") Long recId,
            @RequestParam("page") int page,
            @RequestParam(value = "part", required = false) Part part,
            @RequestParam(value = "status", defaultValue = "SUBMITTED") ApplicationListStatus status
    ) {
        return ApiResponse.success(
                adminApplicationService.getApplicationsByRecruitmentId(recId, part, page, status),
                "지원서 조회 성공"
        );
    }

    @GetMapping("/applications/{appId}")
    public ApiResponse<ApplicationDetailsResponse> getApplicationDetails(
            @PathVariable("appId") Long appId
    ) {
        return ApiResponse.success(
                adminApplicationService.getApplicationDetails(appId),
                "지원서 상세 조회 성공"
        );
    }

    @PatchMapping("/applications/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateApplicationStatus(
            @RequestParam(value = "status") ChangeApplicationStatusParameter status,
            @RequestBody ChangeApplicationStatusRequest req
    ) {
        adminApplicationService.updateApplicationStatus(status, req);
    }

}
