package com.snut_likelion.domain.project.service;

import com.snut_likelion.domain.project.dto.response.RetrospectionResponse;
import com.snut_likelion.domain.project.entity.ProjectRetrospection;
import com.snut_likelion.domain.project.exception.ProjectErrorCode;
import com.snut_likelion.domain.project.infra.ProjectRetrospectionRepository;
import com.snut_likelion.global.auth.model.UserInfo;
import com.snut_likelion.global.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectRetrospectionService {

    private final ProjectRetrospectionRepository projectRetrospectionRepository;

    @Transactional(readOnly = true)
    public List<RetrospectionResponse> getAllByProjectId(Long projectId) {
        return projectRetrospectionRepository.findByProject_Id(projectId).stream()
                .map(RetrospectionResponse::from)
                .toList();
    }

    @Transactional
    @PreAuthorize("@authChecker.isMyProject(#loginUser, #projectId)")
    public void remove(UserInfo loginUser, Long projectId, Long retrospectionId) {
        ProjectRetrospection projectRetrospection = projectRetrospectionRepository.findById(retrospectionId)
                .orElseThrow(() -> new NotFoundException(ProjectErrorCode.NOT_FOUND_RETROSPECTION));
        projectRetrospectionRepository.delete(projectRetrospection);
    }
}
