package com.snut_likelion.domain.blog.service;

import com.snut_likelion.domain.blog.dto.*;
import com.snut_likelion.domain.blog.entity.*;
import com.snut_likelion.domain.blog.exception.BlogErrorCode;
import com.snut_likelion.domain.blog.repository.BlogPostRepository;
import com.snut_likelion.domain.user.entity.User;
import com.snut_likelion.global.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlogQueryService {

    private final BlogPostRepository postRepo;

    // PUBLISHED 목록
    public Page<BlogSummaryResponse> getPostList(Category category,
                                                 int page, int size,
                                                 String keyword) {

        Pageable pageable = PageRequest.of(page, size);

        Page<BlogPost> posts = (keyword == null || keyword.isBlank())
                ? postRepo.findByStatusAndCategoryOrderByUpdatedAtDesc(
                PostStatus.PUBLISHED, category, pageable)
                : postRepo.findByStatusAndCategoryAndTitleContainingIgnoreCaseOrderByUpdatedAtDesc(
                PostStatus.PUBLISHED, category, keyword, pageable);

        return posts.map(this::toSummaryDto);
    }

    // 단건 조회 (PUBLISHED + DRAFT)
    public BlogDetailResponse getPostDetail(Long id) {
        BlogPost p = postRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(BlogErrorCode.POST_NOT_FOUND));
        return toDetailDto(p);
    }

    // DTO 변환
    public BlogSummaryResponse toSummaryDto(BlogPost p) {
        return new BlogSummaryResponse(
                p.getId(), p.getTitle(), p.getUpdatedAt(), p.getThumbnailUrl());
    }

    public BlogDetailResponse toDetailDto(BlogPost p) {
        List<String> imgUrls = p.getImages().stream()
                .map(BlogImage::getUrl)
                .toList();

        return new BlogDetailResponse(
                p.getId(),
                p.getTitle(),
                p.getContent(),
                imgUrls,
                p.getUpdatedAt(),
                p.getAuthor().getUsername(),
                p.getTaggedMembers().stream()
                        .map(User::getUsername)
                        .toList(),
                p.getCategory().name()
        );
    }
}
