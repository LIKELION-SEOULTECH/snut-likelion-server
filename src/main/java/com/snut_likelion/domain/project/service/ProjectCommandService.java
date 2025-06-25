package com.snut_likelion.domain.project.service;

import com.snut_likelion.domain.project.dto.request.CreateProjectRequest;
import com.snut_likelion.domain.project.dto.request.RetrospectionDto;
import com.snut_likelion.domain.project.dto.request.UpdateProjectRequest;
import com.snut_likelion.domain.project.entity.Project;
import com.snut_likelion.domain.project.entity.ProjectParticipation;
import com.snut_likelion.domain.project.entity.ProjectRetrospection;
import com.snut_likelion.domain.project.exception.ProjectErrorCode;
import com.snut_likelion.domain.project.infra.ProjectRepository;
import com.snut_likelion.domain.project.infra.ProjectRetrospectionRepository;
import com.snut_likelion.domain.user.entity.LionInfo;
import com.snut_likelion.domain.user.entity.User;
import com.snut_likelion.domain.user.repository.LionInfoRepository;
import com.snut_likelion.domain.user.repository.UserRepository;
import com.snut_likelion.global.error.exception.BadRequestException;
import com.snut_likelion.global.error.exception.NotFoundException;
import com.snut_likelion.global.provider.FileProvider;
import lombok.RequiredArgsConstructor;
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
        this.connectRetrospections(req.getRetrospections(), project);
        projectRepository.save(project);
    }

    private void connectRetrospections(List<RetrospectionDto> retrospections, Project project) {
        if (retrospections == null || retrospections.isEmpty()) {
            throw new BadRequestException(ProjectErrorCode.RETROSPECTION_IS_NOT_PROVIDED);
        }

        retrospections
                .forEach(retrospectionDto -> {
                    Long memberId = retrospectionDto.getMemberId();

                    LionInfo lionInfo = lionInfoRepository.findByUser_IdAndGeneration(memberId, project.getGeneration())
                            .orElseThrow(() -> new NotFoundException(ProjectErrorCode.NOT_FOUND_LION_INFO));

                    ProjectParticipation projectParticipation = new ProjectParticipation(lionInfo, project);
                    project.addParticipation(projectParticipation);

                    ProjectRetrospection retrospection = this.createRetrospection(retrospectionDto);
                    project.addRetrospection(retrospection);
                });
    }


    @Transactional
    public void modify(Long id, UpdateProjectRequest req) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ProjectErrorCode.NOT_FOUND_PROJECT));

        project.update(req.getName(), req.getIntro(), req.getDescription(), req.getGeneration(), req.getCategory());

        project.setTags(req.getTags());

        this.storeProjectImages(req.getNewImages(), project);

        // 이미 존재하는 회고는 업데이트하고, 새로운 회고는 추가
        this.upsertRetrospections(req.getRetrospections(), project);
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
                                Long memberId = retrospection.getMemberId();
                                LionInfo lionInfo = lionInfoRepository.findByUser_IdAndGeneration(memberId, project.getGeneration())
                                        .orElseThrow(() -> new NotFoundException(ProjectErrorCode.NOT_FOUND_LION_INFO));
                                project.addParticipation(new ProjectParticipation(lionInfo, project));
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
            throw new BadRequestException(ProjectErrorCode.PROJECT_IMAGE_NOT_PROVIDED);
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
    public void remove(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ProjectErrorCode.NOT_FOUND_PROJECT));
        projectRepository.delete(project);
        project.getImageUrlList().forEach(url -> {
            String storedName = fileProvider.extractImageName(url);
            fileProvider.deleteFile(storedName);
        });
    }

    @Transactional
    public void removeImage(Long id, String imageUrl) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ProjectErrorCode.NOT_FOUND_PROJECT));

        ArrayList<String> newList = new ArrayList<>(project.getImageUrlList());
        newList.remove(imageUrl);
        project.setImages(newList);

        String storedName = fileProvider.extractImageName(imageUrl);
        fileProvider.deleteFile(storedName);
    }
}
