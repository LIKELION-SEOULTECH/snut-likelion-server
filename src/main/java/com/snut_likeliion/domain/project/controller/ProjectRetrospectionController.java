package com.snut_likeliion.domain.project.controller;

import com.snut_likeliion.domain.project.dto.request.CreateRetrospectionRequest;
import com.snut_likeliion.domain.project.dto.request.UpdateRetrospectionRequest;
import com.snut_likeliion.domain.project.dto.response.RetrospectionResponse;
import com.snut_likeliion.domain.project.service.ProjectRetrospectionService;
import com.snut_likeliion.global.auth.model.SnutLikeLionUser;
import com.snut_likeliion.global.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects/{projectId}/retrospection")
@RequiredArgsConstructor
public class ProjectRetrospectionController {

    private final ProjectRetrospectionService projectRetrospectionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Object> createProjectRetrospection(
            @AuthenticationPrincipal SnutLikeLionUser writer,
            @PathVariable("projectId") Long projectId,
            @RequestBody @Valid CreateRetrospectionRequest req
    ) {
        projectRetrospectionService.create(projectId, writer.getId(), req);
        return ApiResponse.success("프로젝트 회고 작성 성공");
    }

    @GetMapping
    public ApiResponse<List<RetrospectionResponse>> getProjectRetrospections(
            @PathVariable("projectId") Long projectId
    ) {
        return ApiResponse.success(
                projectRetrospectionService.getAllByProjectId(projectId),
                "프로젝트 회고 목록 조회 성공"
        );
    }

    @PatchMapping("/{retrospectionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void modifyProjectRetrospection(
            @AuthenticationPrincipal SnutLikeLionUser loginUser,
            @PathVariable("projectId") Long projectId,
            @PathVariable("retrospectionId") Long retrospectionId,
            @RequestBody @Valid UpdateRetrospectionRequest req
    ) {
        projectRetrospectionService.modify(loginUser.getUserInfo(), retrospectionId, req);
    }

    @DeleteMapping("/{retrospectionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProjectRetrospection(
            @AuthenticationPrincipal SnutLikeLionUser loginUser,
            @PathVariable("projectId") Long projectId,
            @PathVariable("retrospectionId") Long retrospectionId
    ) {
        projectRetrospectionService.remove(loginUser.getUserInfo(), retrospectionId);
    }
}
