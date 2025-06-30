package com.snut_likelion.domain.blog.controller;

import com.snut_likelion.domain.blog.dto.response.BlogDetailResponse;
import com.snut_likelion.domain.blog.dto.response.BlogSummaryResponse;
import com.snut_likelion.domain.blog.dto.request.CreateBlogRequest;
import com.snut_likelion.domain.blog.dto.request.UpdateBlogRequest;
import com.snut_likelion.domain.blog.entity.Category;
import com.snut_likelion.domain.blog.service.BlogCommandService;
import com.snut_likelion.domain.blog.service.BlogQueryService;
import com.snut_likelion.global.auth.model.SnutLikeLionUser;
import com.snut_likelion.global.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public ApiResponse<Page<BlogSummaryResponse>> getPostList(
            @RequestParam Category category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {

        return ApiResponse.success(queryService.getPostList(category, page, size, keyword));
    }

    // 단건 조회
    @GetMapping(value = "/{id}")
    public ApiResponse<BlogDetailResponse> getPostDetail(@PathVariable Long id) {
        return ApiResponse.success(queryService.getPostDetail(id));
    }

    // 게시글 작성(PUBLISHED)
    @PostMapping
    public ApiResponse<Long> createPost(
            @AuthenticationPrincipal SnutLikeLionUser user,
            @Valid @ModelAttribute CreateBlogRequest req
    ) {
        Long id = commandService.createPost(req, user.getUserInfo());
        return ApiResponse.success(id);
    }

    // 게시글 수정
    @PatchMapping(value = "/{id}")
    public void updatePost(
            @AuthenticationPrincipal SnutLikeLionUser user,
            @PathVariable Long id,
            @ModelAttribute UpdateBlogRequest req
    ) {
        commandService.updatePost(id, req, user.getUserInfo());
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public void deletePost(
            @AuthenticationPrincipal SnutLikeLionUser user,
            @PathVariable Long id
    ) {
        commandService.deletePost(id, user.getUserInfo());
    }

    // 내 임시저장 불러오기
    @GetMapping(value = "/drafts/me")
    public ApiResponse<BlogDetailResponse> getMyDraft(
            @AuthenticationPrincipal SnutLikeLionUser user
    ) {
        return ApiResponse.success(
                queryService.loadDraft(user.getUserInfo()));
    }

    // 임시저장 저장/덮어쓰기
    @PostMapping(value = "/drafts")
    public ApiResponse<Object> saveDraft(
            @AuthenticationPrincipal SnutLikeLionUser user,
            @Valid @ModelAttribute CreateBlogRequest req
    ) {
        commandService.saveDraft(req, user.getUserInfo());
        return ApiResponse.success("임시저장 성공");
    }

    // 임시저장 버리기
    @DeleteMapping("/drafts/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void discardDraft(@AuthenticationPrincipal SnutLikeLionUser user) {
        commandService.discardDraft(user.getUserInfo());
    }
}
