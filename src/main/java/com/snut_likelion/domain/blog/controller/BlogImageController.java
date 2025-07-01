package com.snut_likelion.domain.blog.controller;

import com.snut_likelion.domain.blog.dto.response.UploadBlogImageResponse;
import com.snut_likelion.domain.blog.service.BlogImageCommandService;
import com.snut_likelion.global.dto.ApiResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/blogs/images")
@RequiredArgsConstructor
@Validated
public class BlogImageController {

    private final BlogImageCommandService imgCmdService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER')")
    public ApiResponse<UploadBlogImageResponse> uploadImages(
            @RequestPart("files") @NotNull List<MultipartFile> files) {

        List<String> urls = imgCmdService.upload(files);
        return ApiResponse.success(UploadBlogImageResponse.from(urls));
    }
}