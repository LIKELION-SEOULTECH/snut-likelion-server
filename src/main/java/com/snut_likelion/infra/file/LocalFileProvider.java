package com.snut_likelion.infra.file;

import com.snut_likelion.domain.project.infra.FileProvider;
import com.snut_likelion.global.error.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Slf4j
@Component
public class LocalFileProvider implements FileProvider {

    private final Path rootLocation;

    public LocalFileProvider(@Value("${file.upload-dir}") String uploadDir) {
        this.rootLocation = Paths.get(uploadDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.rootLocation);
        } catch (IOException e) {
            throw new IllegalStateException("파일 저장 디렉토리 생성 실패: " + rootLocation, e);
        }
    }

    @Override
    public Resource getFile(String storedFileName) {
        try {
            Path file = rootLocation.resolve(storedFileName).normalize();
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new BadRequestException(FileErrorCode.FILE_NOT_FOUND);
            }
        } catch (MalformedURLException ex) {
            throw new BadRequestException(FileErrorCode.BAD_FILE_URL, ex.getMessage());
        }
    }

    @Override
    public void deleteFile(String storedFileName) {
        try {
            Path file = rootLocation.resolve(storedFileName).normalize();
            Files.deleteIfExists(file);
        } catch (IOException ex) {
            log.warn("파일 삭제 실패: {}", storedFileName, ex);
        }
    }

    @Override
    public String storeFile(MultipartFile file) {
        // UUID 로 충돌 방지
        String filename = System.currentTimeMillis() + "-" + UUID.randomUUID();
        try (InputStream input = file.getInputStream()) {
            Path destination = rootLocation.resolve(filename);
            Files.copy(input, destination, StandardCopyOption.REPLACE_EXISTING);
            return filename;
        } catch (IOException ex) {
            throw new IllegalStateException("파일 저장 실패: " + filename, ex);
        }
    }

    @Override
    public String extractImageName(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            throw new BadRequestException(FileErrorCode.INVALID_IMAGE_URL);
        }
        String[] parts = imageUrl.split("/");
        if (parts.length == 0) {
            throw new BadRequestException(FileErrorCode.INVALID_IMAGE_URL);
        }

        return parts[parts.length - 1];
    }

    @Override
    public String buildImageUrl(String storedFileName) {
        return String.format("http://localhost:8080/api/v1/images?imageName=%s", storedFileName);
    }
}
