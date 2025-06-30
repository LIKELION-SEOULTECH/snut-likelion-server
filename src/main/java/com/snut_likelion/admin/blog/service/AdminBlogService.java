package com.snut_likelion.admin.blog.service;

import com.snut_likelion.admin.blog.dto.response.BlogPageResponse;
import com.snut_likelion.admin.blog.infra.AdminBlogQueryRepository;
import com.snut_likelion.domain.blog.dto.CreateBlogRequest;
import com.snut_likelion.domain.blog.dto.UpdateBlogRequest;
import com.snut_likelion.domain.blog.service.BlogService;
import com.snut_likelion.global.auth.model.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminBlogService {

    private static final int PAGE_SIZE = 8;

    private final AdminBlogQueryRepository queryRepository;
    private final BlogService              blogService;

    @Transactional(readOnly = true)
    public BlogPageResponse getBlogList(String category, int page, String keyword) {
        PageRequest pageRequest = PageRequest.of(page, PAGE_SIZE);
        Page<BlogPageResponse.BlogListResponse> result =
                queryRepository.getBlogList(category, keyword, pageRequest);
        return BlogPageResponse.from(result);
    }

    @Transactional
    public void create(CreateBlogRequest req, UserInfo info) {
        blogService.createPost(req, info);
    }

    @Transactional
    public void modify(Long id, UpdateBlogRequest req, UserInfo info) {
        blogService.updatePost(id, req, info);
    }

    @Transactional
    public void delete(Long id, UserInfo info) {
        blogService.deletePost(id, info);
    }

    @Transactional
    public void deleteBlogs(List<Long> ids, UserInfo info) {
        ids.forEach(id -> delete(id, info));
    }
}
