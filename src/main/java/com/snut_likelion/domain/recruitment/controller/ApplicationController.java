package com.snut_likelion.domain.recruitment.controller;

import com.snut_likelion.domain.recruitment.dto.request.CreateApplicationRequest;
import com.snut_likelion.domain.recruitment.dto.request.UpdateApplicationRequest;
import com.snut_likelion.domain.recruitment.dto.response.ApplicationDetailsResponse;
import com.snut_likelion.domain.recruitment.service.ApplicationCommandService;
import com.snut_likelion.domain.recruitment.service.ApplicationQueryService;
import com.snut_likelion.global.auth.model.SnutLikeLionUser;
import com.snut_likelion.global.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationCommandService applicationCommandService;
    private final ApplicationQueryService applicationQueryService;

    @GetMapping("/applications/me")
    public ApiResponse<List<ApplicationDetailsResponse>> getMyApplication(
            @AuthenticationPrincipal SnutLikeLionUser loginUser
    ) {
        return ApiResponse.success(
                applicationQueryService.getMyApplication(loginUser.getId()),
                "내 지원서 조회 성공"
        );
    }


    // 임시 저장
    @PostMapping("/recruitments/{recId}/applications")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Object> createApplication(
            @PathVariable("recId") Long recId,
            @AuthenticationPrincipal SnutLikeLionUser loginUser,
            @RequestParam(value = "submit", required = true) boolean submit,
            @ModelAttribute("createApplicationRequest") @Valid CreateApplicationRequest req
    ) {
        applicationCommandService.createApplication(recId, loginUser.getId(), submit, req);
        return ApiResponse.success("지원서 작성 성공");
    }

    @PutMapping("/applications/{appId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateApplication(
            @PathVariable("appId") Long appId,
            @AuthenticationPrincipal SnutLikeLionUser loginUser,
            @RequestParam(value = "submit", required = true) boolean submit,
            @ModelAttribute("updateApplicationRequest") @Valid UpdateApplicationRequest req
    ) {
        applicationCommandService.updateApplication(appId, loginUser.getUserInfo(), submit, req);
    }

    @DeleteMapping("/applications/{appId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteApplication(
            @PathVariable("appId") Long appId,
            @AuthenticationPrincipal SnutLikeLionUser loginUser
    ) {
        applicationCommandService.deleteApplication(appId, loginUser.getUserInfo());
    }

}
