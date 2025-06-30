package com.snut_likelion.domain.project.service;

import com.snut_likelion.domain.project.dto.response.RetrospectionResponse;
import com.snut_likelion.domain.project.entity.Project;
import com.snut_likelion.domain.project.entity.ProjectParticipation;
import com.snut_likelion.domain.project.entity.ProjectRetrospection;
import com.snut_likelion.domain.project.exception.ProjectErrorCode;
import com.snut_likelion.domain.project.infra.ProjectParticipationRepository;
import com.snut_likelion.domain.project.infra.ProjectRetrospectionRepository;
import com.snut_likelion.domain.user.entity.LionInfo;
import com.snut_likelion.domain.user.entity.User;
import com.snut_likelion.global.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectRetrospectionService {

    private final ProjectRetrospectionRepository projectRetrospectionRepository;
    private final ProjectParticipationRepository projectParticipationRepository;

    @Transactional(readOnly = true)
    public List<RetrospectionResponse> getAllByProjectId(Long projectId) {
        return projectRetrospectionRepository.findByProject_Id(projectId).stream()
                .map(RetrospectionResponse::from)
                .toList();
    }

    @Transactional
    public void remove(Long projectId, Long retrospectionId) {
        ProjectRetrospection projectRetrospection = projectRetrospectionRepository.findById(retrospectionId)
                .orElseThrow(() -> new NotFoundException(ProjectErrorCode.NOT_FOUND_RETROSPECTION));

        User writer = projectRetrospection.getWriter();
        Project project = projectRetrospection.getProject();

        LionInfo lionInfo = writer.getLionInfos().stream()
                .filter(li -> li.getGeneration() == project.getGeneration())
                .findFirst()
                .orElseThrow(() -> new NotFoundException(ProjectErrorCode.NOT_FOUND_LION_INFO));

        ProjectParticipation projectParticipation = projectParticipationRepository.findByLionInfo_IdAndProject_Id(lionInfo.getId(), projectId)
                .orElseThrow(() -> new NotFoundException(ProjectErrorCode.NOT_FOUND_PARTICIPANT));

        project.getParticipations().remove(projectParticipation);
        project.getRetrospections().remove(projectRetrospection);
    }
}
