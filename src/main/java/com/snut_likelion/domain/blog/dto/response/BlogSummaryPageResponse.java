package com.snut_likelion.domain.blog.dto.response;

import com.snut_likelion.domain.blog.entity.BlogPost;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BlogSummaryPageResponse {
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private List<BlogSummaryResponse> content;

    @Builder
    public BlogSummaryPageResponse(int page, int size, long totalElements, int totalPages, List<BlogSummaryResponse> content) {
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.content = content;
    }

    public static BlogSummaryPageResponse from(Page<BlogPost> page) {
        return BlogSummaryPageResponse.builder()
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .content(page.getContent().stream().map(BlogSummaryResponse::from).toList())
                .build();
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class BlogSummaryResponse {

        private Long postId;
        private String title;
        private LocalDateTime updatedAt;
        private String thumbnailUrl;

        @Builder
        public BlogSummaryResponse(Long postId, String title, LocalDateTime updatedAt, String thumbnailUrl) {
            this.postId = postId;
            this.title = title;
            this.updatedAt = updatedAt;
            this.thumbnailUrl = thumbnailUrl;
        }

        public static BlogSummaryResponse from(BlogPost blogPost) {
            return BlogSummaryResponse.builder()
                    .postId(blogPost.getId())
                    .title(blogPost.getTitle())
                    .updatedAt(blogPost.getUpdatedAt())
                    .thumbnailUrl(blogPost.getThumbnailUrl())
                    .build();
        }
    }
}
