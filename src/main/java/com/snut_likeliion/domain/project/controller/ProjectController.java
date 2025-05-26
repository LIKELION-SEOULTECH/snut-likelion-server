package com.snut_likeliion.domain.project.controller;

import com.snut_likeliion.domain.project.dto.request.CreateProjectRequest;
import com.snut_likeliion.domain.project.dto.request.UpdateProjectRequest;
import com.snut_likeliion.domain.project.dto.response.ProjectDetailResponse;
import com.snut_likeliion.domain.project.dto.response.ProjectResponse;
import com.snut_likeliion.domain.project.entity.ProjectCategory;
import com.snut_likeliion.domain.project.service.ProjectService;
import com.snut_likeliion.global.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Object> createProject(
            @ModelAttribute @Valid CreateProjectRequest req
    ) {
        projectService.create(req);
        return ApiResponse.success("프로젝트 생성 성공");
    }

    @GetMapping
    public ApiResponse<List<ProjectResponse>> getAllProjects(
            @RequestParam(required = false) Integer generation,
            @RequestParam(required = false) ProjectCategory category
    ) {
        return ApiResponse.success(
                projectService.getAllProjects(generation, category),
                "프로젝트 전체 조회 성공"
        );
    }

    @GetMapping("/{projectId}")
    public ApiResponse<ProjectDetailResponse> getProject(
            @PathVariable("projectId") Long projectId
    ) {
        return ApiResponse.success(projectService.getProjectById(projectId), "프로젝트 상세 조회 성공");
    }

    @PatchMapping("/{projectId}")
    @PreAuthorize("hasRole('MANAGER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void modifyProject(
            @PathVariable("projectId") Long projectId,
            @ModelAttribute("updateProjectRequest") @Valid UpdateProjectRequest req
    ) {
        projectService.modify(projectId, req);
    }

    @DeleteMapping("/{projectId}")
    @PreAuthorize("hasRole('MANAGER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProject(
            @PathVariable("projectId") Long projectId
    ) {
        projectService.remove(projectId);
    }

}
