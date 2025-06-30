package com.snut_likelion.domain.blog.service;

import com.snut_likelion.domain.blog.exception.BlogErrorCode;
import com.snut_likelion.domain.blog.exception.BlogException;
import com.snut_likelion.global.provider.FileProvider;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BlogImageCommandService {

    private final FileProvider fileProvider;

    public List<String> upload(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            throw new BlogException(BlogErrorCode.FILES_REQUIRED);
        }

        try {
            return files.stream()
                    .map(fileProvider::storeFile)
                    .map(fileProvider::buildImageUrl)
                    .toList();
        } catch (Exception e) {
            throw new BlogException(BlogErrorCode.IMAGE_UPLOAD_FAILED, e);
        }
    }
}
