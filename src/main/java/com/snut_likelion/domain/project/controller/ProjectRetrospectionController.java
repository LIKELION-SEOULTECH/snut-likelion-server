package com.snut_likelion.domain.project.controller;

import com.snut_likelion.domain.project.dto.response.RetrospectionResponse;
import com.snut_likelion.domain.project.service.ProjectRetrospectionService;
import com.snut_likelion.global.auth.model.SnutLikeLionUser;
import com.snut_likelion.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects/{projectId}/retrospections")
@RequiredArgsConstructor
public class ProjectRetrospectionController {

    private final ProjectRetrospectionService projectRetrospectionService;

    @GetMapping
    public ApiResponse<List<RetrospectionResponse>> getProjectRetrospections(
            @PathVariable("projectId") Long projectId
    ) {
        return ApiResponse.success(
                projectRetrospectionService.getAllByProjectId(projectId),
                "프로젝트 회고 목록 조회 성공"
        );
    }

    @DeleteMapping("/{retrospectionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("@authChecker.isMyProject(#loginUser.userInfo, #projectId)")
    public void deleteProjectRetrospection(
            @AuthenticationPrincipal SnutLikeLionUser loginUser,
            @PathVariable("projectId") Long projectId,
            @PathVariable("retrospectionId") Long retrospectionId
    ) {
<<<<<<< HEAD
        projectRetrospectionService.remove(projectId, retrospectionId);
=======
        projectRetrospectionService.remove(loginUser.getUserInfo(), projectId, retrospectionId);
>>>>>>> 6de69ba85508f0cbec27e57958f0783643f34360
    }
}
