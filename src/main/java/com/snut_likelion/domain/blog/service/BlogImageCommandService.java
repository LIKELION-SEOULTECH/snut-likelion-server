package com.snut_likelion.domain.blog.service;

import com.snut_likelion.domain.blog.exception.BlogErrorCode;
import com.snut_likelion.global.error.exception.BadRequestException;
import com.snut_likelion.global.provider.FileProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BlogImageCommandService {

    private final FileProvider fileProvider;

    public List<String> upload(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            throw new BadRequestException(BlogErrorCode.FILES_REQUIRED);
        }

        return files.stream()
                .map(fileProvider::storeFile)
                .map(fileProvider::buildImageUrl)
                .toList();
    }
}
