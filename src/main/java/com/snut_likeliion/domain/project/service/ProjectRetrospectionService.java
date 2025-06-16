package com.snut_likeliion.domain.project.service;

import com.snut_likeliion.domain.project.dto.response.RetrospectionResponse;
import com.snut_likeliion.domain.project.entity.ProjectRetrospection;
import com.snut_likeliion.domain.project.exception.ProjectErrorCode;
import com.snut_likeliion.domain.project.infra.ProjectRetrospectionRepository;
import com.snut_likeliion.global.auth.model.UserInfo;
import com.snut_likeliion.global.error.exception.NotFoundException;
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
    @PreAuthorize("@authChecker.isMyProject(#loginUser, #retrospectionId)")
    public void remove(UserInfo loginUser, Long retrospectionId) {
        ProjectRetrospection projectRetrospection = projectRetrospectionRepository.findById(retrospectionId)
                .orElseThrow(() -> new NotFoundException(ProjectErrorCode.NOT_FOUND_RETROSPECTION));
        projectRetrospectionRepository.delete(projectRetrospection);
    }
}
