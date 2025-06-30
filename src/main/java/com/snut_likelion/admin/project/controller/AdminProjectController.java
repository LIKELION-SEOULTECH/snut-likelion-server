package com.snut_likelion.admin.project.controller;

import com.snut_likelion.admin.project.dto.response.ProjectPageResponse;
import com.snut_likelion.admin.project.service.AdminProjectService;
import com.snut_likelion.domain.project.dto.request.CreateProjectRequest;
import com.snut_likelion.domain.project.dto.request.UpdateProjectRequest;
import com.snut_likelion.domain.project.dto.response.RetrospectionResponse;
import com.snut_likelion.global.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/projects")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_MANAGER')")
public class AdminProjectController {

    private final AdminProjectService adminProjectService;

    @GetMapping
    public ApiResponse<ProjectPageResponse> getProjectList(
            @RequestParam(value = "generation", required = false) Integer generation,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "keyword", required = false) String keyword
    ) {
        return ApiResponse.success(
                adminProjectService.getProjectList(generation, page, keyword),
                "프로젝트 리스트 조회 성공"
        );
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProjects(
            @RequestParam("ids") List<Long> ids
    ) {
        adminProjectService.deleteProjects(ids);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Object> createProject(
            @ModelAttribute @Valid CreateProjectRequest req
    ) {
        adminProjectService.create(req);
        return ApiResponse.success("프로젝트 생성 성공");
    }

    @PatchMapping("/{projectId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void modifyProject(
            @PathVariable("projectId") Long projectId,
            @ModelAttribute("updateProjectRequest") @Valid UpdateProjectRequest req
    ) {
        adminProjectService.modify(projectId, req);
    }

    @DeleteMapping("/{projectId}/images")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProjectImage(
            @PathVariable("projectId") Long projectId,
            @RequestParam("imageUrl") String imageUrl
    ) {
        adminProjectService.removeImage(projectId, imageUrl);
    }

    @GetMapping("/{projectId}/retrospections")
    public ApiResponse<List<RetrospectionResponse>> getProjectRetrospections(
            @PathVariable("projectId") Long projectId
    ) {
        return ApiResponse.success(
                adminProjectService.getAllRetrospectionsByProjectId(projectId),
                "프로젝트 회고 목록 조회 성공"
        );
    }

    @DeleteMapping("/{projectId}/retrospections/{retrospectionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProjectRetrospection(
            @PathVariable("projectId") Long projectId,
            @PathVariable("retrospectionId") Long retrospectionId
    ) {
        adminProjectService.removeRetrospection(projectId, retrospectionId);
    }
}
