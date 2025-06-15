package com.snut_likeliion.domain.project.infra;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileProvider {

    Resource getFile(String storedFileName);

    void deleteFile(String storedFileName);

    String storeFile(MultipartFile file);

    String extractImageName(String imageUrl);

    String buildImageUrl(String storedFileName);
}
