package com.snut_likeliion.domain.blog.controller;

import com.snut_likeliion.domain.blog.dto.CreateBlogRequest;
import com.snut_likeliion.domain.blog.dto.UpdateBlogRequest;
import com.snut_likeliion.domain.blog.service.BlogService;
import com.snut_likeliion.global.auth.model.SnutLikeLionUser;
import com.snut_likeliion.global.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/blogs")
public class BlogController {

    private final BlogService blogService;

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public ApiResponse<Long> createBlog(@AuthenticationPrincipal SnutLikeLionUser user,
                                        @Valid @ModelAttribute CreateBlogRequest request) {

        Long id = blogService.createPost(request, user.getUserInfo());

        return ApiResponse.success(id);
    }

    @PatchMapping(value = "/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public ApiResponse<Long> updateBlog(@AuthenticationPrincipal SnutLikeLionUser user,
                                        @PathVariable Long postId,
                                        @Valid @ModelAttribute UpdateBlogRequest request) {
        Long id = blogService.updatePost(postId, request, user.getUserInfo());

        return ApiResponse.success(id);
    }

    @DeleteMapping("/{blogId}")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public ApiResponse<Void> deleteBlog(@AuthenticationPrincipal SnutLikeLionUser user,
                                        @PathVariable Long blogId) {

        blogService.deletePost(blogId, user.getUserInfo());

        return ApiResponse.success(null, "게시글 삭제 성공");
    }
}
