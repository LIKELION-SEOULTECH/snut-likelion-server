package com.snut_likeliion.domain.project.service;

import com.snut_likeliion.domain.project.dto.response.ProjectDetailResponse;
import com.snut_likeliion.domain.project.dto.response.ProjectResponse;
import com.snut_likeliion.domain.project.entity.Project;
import com.snut_likeliion.domain.project.entity.ProjectCategory;
import com.snut_likeliion.domain.project.exception.ProjectErrorCode;
import com.snut_likeliion.domain.project.infra.ProjectQueryRepository;
import com.snut_likeliion.domain.project.infra.ProjectRepository;
import com.snut_likeliion.global.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectQueryService {

    private final ProjectRepository projectRepository;
    private final ProjectQueryRepository projectQueryRepository;

    public List<ProjectResponse> getAllProjects(Integer generation, ProjectCategory category) {
        List<Project> projects = projectQueryRepository.findAllByGenerationAndCategory(generation, category);
        return projects.stream().map(ProjectResponse::from).toList();
    }

    public ProjectDetailResponse getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ProjectErrorCode.NOT_FOUND_PROJECT));

        return ProjectDetailResponse.from(project);
    }
}
