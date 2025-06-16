package com.snut_likelion.domain.project.controller;

import com.snut_likelion.domain.project.dto.request.CreateProjectRequest;
import com.snut_likelion.domain.project.dto.request.UpdateProjectRequest;
import com.snut_likelion.domain.project.dto.response.ProjectDetailResponse;
import com.snut_likelion.domain.project.dto.response.ProjectResponse;
import com.snut_likelion.domain.project.entity.ProjectCategory;
import com.snut_likelion.domain.project.service.ProjectCommandService;
import com.snut_likelion.domain.project.service.ProjectQueryService;
import com.snut_likelion.global.auth.model.SnutLikeLionUser;
import com.snut_likelion.global.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectCommandService projectCommandService;
    private final ProjectQueryService projectQueryService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Object> createProject(
            @ModelAttribute @Valid CreateProjectRequest req
    ) {
        projectCommandService.create(req);
        return ApiResponse.success("프로젝트 생성 성공");
    }

    @GetMapping
    public ApiResponse<List<ProjectResponse>> getAllProjects(
            @RequestParam(required = false) Integer generation,
            @RequestParam(required = false) ProjectCategory category
    ) {
        return ApiResponse.success(
                projectQueryService.getAllProjects(generation, category),
                "프로젝트 전체 조회 성공"
        );
    }

    @GetMapping("/{projectId}")
    public ApiResponse<ProjectDetailResponse> getProject(
            @PathVariable("projectId") Long projectId
    ) {
        return ApiResponse.success(projectQueryService.getProjectById(projectId), "프로젝트 상세 조회 성공");
    }

    @PatchMapping("/{projectId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void modifyProject(
            @AuthenticationPrincipal SnutLikeLionUser loginUser,
            @PathVariable("projectId") Long projectId,
            @ModelAttribute("updateProjectRequest") @Valid UpdateProjectRequest req
    ) {
        projectCommandService.modify(loginUser.getUserInfo(), projectId, req);
    }

    @DeleteMapping("/{projectId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProject(
            @AuthenticationPrincipal SnutLikeLionUser loginUser,
            @PathVariable("projectId") Long projectId
    ) {
        projectCommandService.remove(loginUser.getUserInfo(), projectId);
    }

    @DeleteMapping("/{projectId}/images")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProjectImage(
            @AuthenticationPrincipal SnutLikeLionUser loginUser,
            @PathVariable("projectId") Long projectId,
            @RequestParam("imageUrl") String imageUrl
    ) {
        projectCommandService.removeImage(loginUser.getUserInfo(), projectId, imageUrl);
    }

}
