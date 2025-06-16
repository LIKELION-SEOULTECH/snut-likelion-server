package com.snut_likeliion.domain.project.service;

import com.snut_likeliion.domain.project.dto.request.CreateProjectRequest;
import com.snut_likeliion.domain.project.dto.request.RetrospectionDto;
import com.snut_likeliion.domain.project.dto.request.UpdateProjectRequest;
import com.snut_likeliion.domain.project.entity.Project;
import com.snut_likeliion.domain.project.entity.ProjectParticipation;
import com.snut_likeliion.domain.project.entity.ProjectRetrospection;
import com.snut_likeliion.domain.project.exception.ProjectErrorCode;
import com.snut_likeliion.domain.project.infra.FileProvider;
import com.snut_likeliion.domain.project.infra.ProjectRepository;
import com.snut_likeliion.domain.project.infra.ProjectRetrospectionRepository;
import com.snut_likeliion.domain.user.entity.User;
import com.snut_likeliion.domain.user.repository.LionInfoRepository;
import com.snut_likeliion.domain.user.repository.UserRepository;
import com.snut_likeliion.global.auth.model.UserInfo;
import com.snut_likeliion.global.error.exception.BadRequestException;
import com.snut_likeliion.global.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectCommandService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final FileProvider fileProvider;
    private final LionInfoRepository lionInfoRepository;
    private final ProjectRetrospectionRepository projectRetrospectionRepository;

    @Transactional
    public void create(CreateProjectRequest req) {
        Project project = req.toEntityWithValue();
        project.setTags(req.getTags());
        this.storeProjectImages(req.getImages(), project);
        this.connectParticipants(req.getMemberIds(), project);
        this.connectRetrospections(req.getRetrospections(), project);
        projectRepository.save(project);
    }

    private void connectRetrospections(List<RetrospectionDto> retrospections, Project project) {
        if (retrospections == null || retrospections.isEmpty()) {
            return; // 회고가 없으면 그냥 리턴
        }

        retrospections.stream()
                .map(this::createRetrospection)
                .forEach(project::addRetrospection);
    }

    private void connectParticipants(List<Long> memberIds, Project project) {
        if (memberIds == null || memberIds.isEmpty()) {
            throw new BadRequestException(ProjectErrorCode.MEMBER_IDS_NOT_PROVIDED);
        }

        memberIds.stream()
                .map(memberId -> lionInfoRepository.findByUser_IdAndGeneration(memberId, project.getGeneration())
                        .orElseThrow(() -> new NotFoundException(ProjectErrorCode.NOT_FOUND_LION_INFO)))
                .map(li -> new ProjectParticipation(li, project))
                .forEach(project::addParticipation);
    }


    @Transactional
    @PreAuthorize("@authChecker.isMyProject(#loginUser, #id)")
    public void modify(UserInfo loginUser, Long id, UpdateProjectRequest req) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ProjectErrorCode.NOT_FOUND_PROJECT));

        project.update(req.getName(), req.getIntro(), req.getDescription(), req.getGeneration(), req.getCategory());

        project.setTags(req.getTags());

        if (req.getMemberIds() != null && !req.getMemberIds().isEmpty()) {
            project.getParticipations().clear();
            this.connectParticipants(req.getMemberIds(), project);
        }

        // 이미 존재하는 회고는 업데이트하고, 새로운 회고는 추가
        this.upsertRetrospections(req.getRetrospections(), project);

        this.storeProjectImages(req.getNewImages(), project);
    }

    private void upsertRetrospections(List<RetrospectionDto> retrospections, Project project) {
        if (retrospections == null || retrospections.isEmpty()) {
            return;
        }

        retrospections.forEach(retrospection -> {
            projectRetrospectionRepository.findByWriter_IdAndProject_Id(
                            retrospection.getMemberId(), project.getId())
                    .ifPresentOrElse(
                            existingRetrospection ->
                                    existingRetrospection.updateContent(retrospection.getContent()),
                            () -> {
                                project.addRetrospection(this.createRetrospection(retrospection));
                            }
                    );
        });
    }

    private ProjectRetrospection createRetrospection(RetrospectionDto retrospection) {
        User writer = userRepository.findById(retrospection.getMemberId())
                .orElseThrow(() -> new NotFoundException(ProjectErrorCode.NOT_FOUND_PARTICIPANT));
        ProjectRetrospection projectRetrospection = new ProjectRetrospection(retrospection.getContent());
        projectRetrospection.setWriter(writer);
        return projectRetrospection;
    }

    private void storeProjectImages(List<MultipartFile> files, Project project) {
        if (files == null || files.isEmpty()) {
            return; // 이미지가 없으면 그냥 리턴
        }

        List<String> imageUrls = new ArrayList<>();

        files.forEach(file -> {
            String contentType = file.getContentType();

            if (!contentType.startsWith("image/")) {
                throw new BadRequestException(ProjectErrorCode.INVALID_FILE_FORMAT);
            }

            String storedName = fileProvider.storeFile(file);
            fileProvider.setTransactionSynchronizationForImage(storedName);
            imageUrls.add(fileProvider.buildImageUrl(storedName));
        });

        project.addImage(imageUrls);
    }

    @Transactional
    @PreAuthorize("@authChecker.isMyProject(#loginUser, #id)")
    public void remove(UserInfo loginUser, Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ProjectErrorCode.NOT_FOUND_PROJECT));
        projectRepository.delete(project);
        project.getImageUrlList().forEach(url -> {
            String storedName = fileProvider.extractImageName(url);
            fileProvider.deleteFile(storedName);
        });
    }

    @Transactional
    @PreAuthorize("@authChecker.isMyProject(#loginUser, #id)")
    public void removeImage(UserInfo loginUser, Long id, String imageUrl) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ProjectErrorCode.NOT_FOUND_PROJECT));

        ArrayList<String> newList = new ArrayList<>(project.getImageUrlList());
        newList.remove(imageUrl);
        project.setImages(newList);

        String storedName = fileProvider.extractImageName(imageUrl);
        fileProvider.deleteFile(storedName);
    }
}
