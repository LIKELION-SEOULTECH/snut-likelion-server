package com.snut_likelion.domain.blog.dto;

import com.snut_likelion.domain.blog.entity.BlogPost;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

//  블로그 목록 조회 DTO -> 제목, 수정일, 썸네일만 전달
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BlogSummaryResponse {

    private Long postId;
    private String title;
    private LocalDateTime updatedAt;
    private String thumbnailUr;

    @Builder
    public BlogSummaryResponse(Long postId, String title, LocalDateTime updatedAt, String thumbnailUr) {
        this.postId = postId;
        this.title = title;
        this.updatedAt = updatedAt;
        this.thumbnailUr = thumbnailUr;
    }

    public static BlogSummaryResponse from(BlogPost blogPost) {
        return BlogSummaryResponse.builder()
                .postId(blogPost.getId())
                .title(blogPost.getTitle())
                .updatedAt(blogPost.getUpdatedAt())
                .thumbnailUr(blogPost.getThumbnailUrl())
                .build();
    }
}
