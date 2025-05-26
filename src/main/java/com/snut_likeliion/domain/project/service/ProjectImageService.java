package com.snut_likeliion.domain.project.service;

import com.snut_likeliion.domain.project.entity.ProjectImage;
import com.snut_likeliion.domain.project.exception.ProjectErrorCode;
import com.snut_likeliion.domain.project.infra.FileProvider;
import com.snut_likeliion.domain.project.infra.ProjectImageRepository;
import com.snut_likeliion.global.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectImageService {

    private final ProjectImageRepository projectImageRepository;
    private final FileProvider fileProvider;

    public Resource getImage(String imageName) {
        return fileProvider.getFile(imageName);
    }

    public void removeImage(Long projectId, String imageName) {
        ProjectImage projectImage = projectImageRepository.findByProject_IdAndOriginalName(projectId, imageName)
                .orElseThrow(() -> new NotFoundException(ProjectErrorCode.NOT_FOUND_IMAGE));
        projectImageRepository.delete(projectImage);
        fileProvider.deleteFile(projectImage.getStoredName());
    }
}
