package com.snut_likelion.domain.blog.controller;

import com.snut_likelion.domain.blog.dto.*;
import com.snut_likelion.domain.blog.entity.Category;
import com.snut_likelion.domain.blog.service.*;
import com.snut_likelion.global.auth.model.SnutLikeLionUser;
import com.snut_likelion.global.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/blogs")
@RequiredArgsConstructor
public class BlogController {

    private final BlogService      blogService;
    private final BlogQueryService queryService;

    // 게시글(PUBLISHED) 목록
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<Page<BlogSummaryResponse>> getPostList(
            @RequestParam Category category,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false)    String keyword) {

        return ApiResponse.success(queryService.getPostList(category, page, size, keyword));
    }

    // 단건 조회
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<BlogDetailResponse> getPostDetail(@PathVariable Long id) {
        return ApiResponse.success(queryService.getPostDetail(id));
    }

    // 게시글 작성(PUBLISHED)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('USER','MANAGER','ADMIN')")
    public ApiResponse<Long> createPost(@AuthenticationPrincipal SnutLikeLionUser user,
                                        @Valid @ModelAttribute CreateBlogRequest req) {

        Long id = blogService.createPost(req, user.getUserInfo());
        return ApiResponse.success(id);
    }

    // 게시글 수정
    @PatchMapping(value = "/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('USER','MANAGER','ADMIN')")
    public ApiResponse<Long> updatePost(@AuthenticationPrincipal SnutLikeLionUser user,
                                        @PathVariable Long id,
                                        @ModelAttribute UpdateBlogRequest req) {

        Long postId = blogService.updatePost(id, req, user.getUserInfo());
        return ApiResponse.success(postId);
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','MANAGER','ADMIN')")
    public ApiResponse<Void> deletePost(@AuthenticationPrincipal SnutLikeLionUser user,
                                        @PathVariable Long id) {

        blogService.deletePost(id, user.getUserInfo());
        return ApiResponse.success(null, "게시글 삭제 성공");
    }

    // 내 임시저장 불러오기
    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/drafts/me",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<BlogDetailResponse> getMyDraft(
            @AuthenticationPrincipal SnutLikeLionUser user) {

        return ApiResponse.success(
                blogService.loadDraft(user.getUserInfo()));
    }

    // 임시저장 저장/덮어쓰기
    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/drafts",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<Long> saveDraft(@AuthenticationPrincipal SnutLikeLionUser user,
                                       @Valid @ModelAttribute CreateBlogRequest req) {

        Long id = blogService.saveDraft(req, user.getUserInfo());
        return ApiResponse.success(id);
    }

    // 임시저장 버리기
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/drafts/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void discardDraft(@AuthenticationPrincipal SnutLikeLionUser user) {
        blogService.discardDraft(user.getUserInfo());
    }
}
