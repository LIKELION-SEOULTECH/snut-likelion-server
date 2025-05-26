package com.snut_likeliion.domain.project.service;

import com.snut_likeliion.domain.project.dto.request.CreateRetrospectionRequest;
import com.snut_likeliion.domain.project.dto.request.UpdateRetrospectionRequest;
import com.snut_likeliion.domain.project.dto.response.RetrospectionResponse;
import com.snut_likeliion.domain.project.entity.Project;
import com.snut_likeliion.domain.project.entity.ProjectRetrospection;
import com.snut_likeliion.domain.project.exception.ProjectErrorCode;
import com.snut_likeliion.domain.project.infra.ProjectRepository;
import com.snut_likeliion.domain.project.infra.ProjectRetrospectionRepository;
import com.snut_likeliion.domain.user.entity.User;
import com.snut_likeliion.domain.user.repository.UserRepository;
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

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectRetrospectionRepository projectRetrospectionRepository;

    @Transactional
    public void create(Long projectId, Long writerId, CreateRetrospectionRequest req) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException(ProjectErrorCode.NOT_FOUND_PROJECT));

        User writer = userRepository.findById(writerId)
                .orElseThrow(() -> new NotFoundException(ProjectErrorCode.NOT_FOUND_PARTICIPANT));

        ProjectRetrospection projectRetrospection = ProjectRetrospection.of(req.getContent(), writer, project);
        projectRetrospectionRepository.save(projectRetrospection);
    }

    public List<RetrospectionResponse> getAllByProjectId(Long projectId) {
        return projectRetrospectionRepository.findByProject_Id(projectId).stream()
                .map(RetrospectionResponse::from)
                .toList();
    }

    @Transactional
    @PreAuthorize("@authChecker.isMyRetrospection(#loginUser, #retrospectionId)")
    public void modify(UserInfo loginUser, Long retrospectionId, UpdateRetrospectionRequest req) {
        ProjectRetrospection projectRetrospection = projectRetrospectionRepository.findById(retrospectionId)
                .orElseThrow(() -> new NotFoundException(ProjectErrorCode.NOT_FOUND_RETROSPECTION));

        projectRetrospection.updateContent(req.getContent());
    }

    @Transactional
    @PreAuthorize("@authChecker.isMyRetrospection(#loginUser, #retrospectionId)")
    public void remove(UserInfo loginUser, Long retrospectionId) {
        ProjectRetrospection projectRetrospection = projectRetrospectionRepository.findById(retrospectionId)
                .orElseThrow(() -> new NotFoundException(ProjectErrorCode.NOT_FOUND_RETROSPECTION));
        projectRetrospectionRepository.delete(projectRetrospection);
    }
}
