package com.snut_likelion.domain.blog.controller;

import com.snut_likelion.domain.blog.dto.request.CreateBlogRequest;
import com.snut_likelion.domain.blog.dto.request.UpdateBlogRequest;
import com.snut_likelion.domain.blog.dto.response.BlogDetailResponse;
import com.snut_likelion.domain.blog.dto.response.BlogSummaryPageResponse;
import com.snut_likelion.domain.blog.entity.Category;
import com.snut_likelion.domain.blog.service.BlogCommandService;
import com.snut_likelion.domain.blog.service.BlogQueryService;
import com.snut_likelion.global.auth.model.SnutLikeLionUser;
import com.snut_likelion.global.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/blogs")
@RequiredArgsConstructor
public class BlogController {

    private final BlogCommandService commandService;
    private final BlogQueryService queryService;

    // 게시글(PUBLISHED) 목록
    @GetMapping
    public ApiResponse<BlogSummaryPageResponse> getPostList(
            @RequestParam Category category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {

        return ApiResponse.success(queryService.getPostList(category, page, size, keyword));
    }

    // 단건 조회
    @GetMapping("/{blogId}")
    public ApiResponse<BlogDetailResponse> getPostDetail(@PathVariable("blogId") Long blogId) {
        return ApiResponse.success(queryService.getPostDetail(blogId));
    }

    // 게시글 작성(PUBLISHED)
    @PostMapping
    public ApiResponse<Long> createPost(
            @AuthenticationPrincipal SnutLikeLionUser user,
            @RequestParam(value = "submit") boolean submit,
            @RequestBody @Valid CreateBlogRequest req
    ) {
        Long id = commandService.createPost(req, user.getUserInfo(), submit);
        return ApiResponse.success(id);
    }

    // 게시글 수정
    @PatchMapping(value = "/{blogId}")
    public void updatePost(
            @AuthenticationPrincipal SnutLikeLionUser user,
            @PathVariable("blogId") Long blogId,
            @RequestParam(value = "submit") boolean submit,
            @RequestBody @Valid UpdateBlogRequest req
    ) {
        commandService.updatePost(blogId, req, user.getUserInfo(), submit);
    }

    // 게시글 삭제
    @DeleteMapping("/{blogId}")
    public void deletePost(
            @AuthenticationPrincipal SnutLikeLionUser user,
            @PathVariable("blogId") Long blogId
    ) {
        commandService.deletePost(blogId, user.getUserInfo());
    }

    // 내가 쓴 글(PUBLISHED) 목록
    @GetMapping("/me")
    public ApiResponse<BlogSummaryPageResponse> getMyPosts(
            @AuthenticationPrincipal SnutLikeLionUser user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(
                queryService.getMyPosts(user.getUserInfo(), page, size)
        );
    }

    // 내 임시저장 불러오기
    @GetMapping(value = "/drafts/me")
    public ApiResponse<BlogDetailResponse> getMyDraft(
            @AuthenticationPrincipal SnutLikeLionUser user
    ) {
        return ApiResponse.success(
                queryService.loadDraft(user.getUserInfo()));
    }

    // 임시저장 버리기
    @DeleteMapping("/drafts/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void discardDraft(@AuthenticationPrincipal SnutLikeLionUser user) {
        commandService.discardDraft(user.getUserInfo());
    }
}
