package com.snut_likelion.domain.blog.controller;

import com.snut_likelion.domain.blog.dto.response.UploadBlogImageResponse;
import com.snut_likelion.domain.blog.service.BlogImageCommandService;
import com.snut_likelion.global.dto.ApiResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/blogs/images")
@RequiredArgsConstructor
public class BlogImageController {

    private final BlogImageCommandService imgCmdService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER')")
    public ApiResponse<UploadBlogImageResponse> uploadImages(
            @RequestPart("files") @NotNull List<MultipartFile> files) {

        List<String> urls = imgCmdService.upload(files);
        return ApiResponse.success(UploadBlogImageResponse.from(urls));
    }
}