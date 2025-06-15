package com.snut_likeliion.domain.project.service;

import com.snut_likeliion.domain.project.dto.request.CreateProjectRequest;
import com.snut_likeliion.domain.project.dto.request.UpdateProjectRequest;
import com.snut_likeliion.domain.project.dto.response.ProjectDetailResponse;
import com.snut_likeliion.domain.project.dto.response.ProjectResponse;
import com.snut_likeliion.domain.project.entity.*;
import com.snut_likeliion.domain.project.exception.ProjectErrorCode;
import com.snut_likeliion.domain.project.infra.*;
import com.snut_likeliion.domain.user.repository.UserRepository;
import com.snut_likeliion.global.error.exception.BadRequestException;
import com.snut_likeliion.global.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectKeywordRepository projectKeywordRepository;
    private final ProjectImageRepository projectImageRepository;
    private final ProjectParticipationRepository projectParticipationRepository;
    private final ProjectQueryRepository projectQueryRepository;
    private final UserRepository userRepository;
    private final FileProvider fileProvider;

    @Transactional
    public void create(CreateProjectRequest req) {
        Project project = req.toEntityWithValue();
        this.connectKeywords(req.getKeywords(), project);
        this.connectProjectImages(req.getImages(), project);
        this.connectParticipants(req.getMembers(), project);
        projectRepository.save(project);
    }

    private void connectKeywords(List<String> keywords, Project project) {
        List<ProjectKeyword> projectKeywords = keywords.stream().map(ProjectKeyword::new).toList();
        project.setKeywords(projectKeywords);
        projectKeywordRepository.saveAll(projectKeywords);
    }

    private void connectProjectImages(List<MultipartFile> files, Project project) {
        List<ProjectImage> projectImages = this.storeFilesIfPresent(files);
        if (!projectImages.isEmpty()) {
            projectImages.forEach(project::addImage);
        }
        projectImageRepository.saveAll(projectImages);
    }

    private void connectParticipants(List<Long> members, Project project) {
        List<ProjectParticipation> participants = members.stream()
                .map(memberId ->
                        userRepository.findById(memberId)
                                .orElseThrow(() -> new NotFoundException(ProjectErrorCode.NOT_FOUND_PARTICIPANT)))
                .map(user -> new ProjectParticipation(user.getLionInfos().get(0), project))
                .toList();
        project.setParticipants(participants);
        projectParticipationRepository.saveAll(participants);
    }

    private List<ProjectImage> storeFilesIfPresent(List<MultipartFile> files) {
        if (files != null && !files.isEmpty()) {
            return files.stream()
                    .map(file -> {
                        String contentType = file.getContentType();

                        if (!contentType.startsWith("image/")) {
                            throw new BadRequestException(ProjectErrorCode.INVALID_FILE_FORMAT);
                        }

                        String storedName = fileProvider.storeFile(file);
                        return ProjectImage.of(file.getOriginalFilename(), storedName);
                    })
                    .toList();
        }

        return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public List<ProjectResponse> getAllProjects(Integer generation, ProjectCategory category) {
        List<Project> projects = projectQueryRepository.findAllByGenerationAndCategory(generation, category);
        return projects.stream().map(ProjectResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public ProjectDetailResponse getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ProjectErrorCode.NOT_FOUND_PROJECT));

        return ProjectDetailResponse.from(project);
    }

    @Transactional
    public void modify(Long id, UpdateProjectRequest req) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ProjectErrorCode.NOT_FOUND_PROJECT));

        project.update(req.getName(), req.getIntro(), req.getDescription(), req.getGeneration(), req.getCategory());

        if (req.getKeywords() != null && !req.getKeywords().isEmpty()) {
            project.getKeywords().clear();
            this.connectKeywords(req.getKeywords(), project);
        }

        if (req.getMembers() != null && !req.getMembers().isEmpty()) {
            project.getParticipations().clear();
            this.connectParticipants(req.getMembers(), project);
        }

        this.connectProjectImages(req.getNewImages(), project);
    }

    @Transactional
    public void remove(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ProjectErrorCode.NOT_FOUND_PROJECT));
        projectRepository.delete(project);
        project.getImages().forEach(projectImage -> fileProvider.deleteFile(projectImage.getStoredName()));
    }
}
