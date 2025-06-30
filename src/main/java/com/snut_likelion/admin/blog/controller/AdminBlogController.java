package com.snut_likelion.admin.blog.controller;

import com.snut_likelion.admin.blog.dto.response.BlogPageResponse;
import com.snut_likelion.admin.blog.service.AdminBlogService;
import com.snut_likelion.domain.blog.dto.request.CreateBlogRequest;
import com.snut_likelion.domain.blog.dto.request.UpdateBlogRequest;
import com.snut_likelion.global.auth.model.SnutLikeLionUser;
import com.snut_likelion.global.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/blogs")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_MANAGER')")
public class AdminBlogController {

    private final AdminBlogService blogService;

    @GetMapping
    public ApiResponse<BlogPageResponse> getBlogList(
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "page",      defaultValue = "0") int page,
            @RequestParam(value = "keyword",   required = false) String keyword
    ) {
        return ApiResponse.success(
                blogService.getBlogList(category, page, keyword),
                "블로그 리스트 조회 성공"
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Object> createBlog(
            @AuthenticationPrincipal SnutLikeLionUser user,
            @ModelAttribute @Valid CreateBlogRequest req
    ) {
        blogService.create(req, user.getUserInfo());
        return ApiResponse.success("블로그 업로드 성공");
    }

    @PatchMapping("/{blogId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void modifyBlog(
            @AuthenticationPrincipal SnutLikeLionUser user,
            @PathVariable Long blogId,
            @ModelAttribute @Valid UpdateBlogRequest req
    ) {
        blogService.modify(blogId, req, user.getUserInfo());
    }

    @DeleteMapping("/{blogId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBlog(
            @AuthenticationPrincipal SnutLikeLionUser user,
            @PathVariable Long blogId
    ) {
        blogService.delete(blogId, user.getUserInfo());
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBlogs(
            @AuthenticationPrincipal SnutLikeLionUser user,
            @RequestParam("ids") List<Long> ids
    ) {
        blogService.deleteBlogs(ids, user.getUserInfo());
    }
}
