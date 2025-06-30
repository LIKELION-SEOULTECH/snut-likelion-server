package com.snut_likelion.domain.blog.dto.response;

import com.snut_likelion.domain.blog.entity.BlogImage;
import com.snut_likelion.domain.blog.entity.BlogPost;
import com.snut_likelion.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

// 블로그 단건 조회 DTO -> 썸네일은 제외하고 본문 HTML + 이미지 URL 전체 전달
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BlogDetailResponse {
    private Long postId;
    private String title;
    private String contentHtml;
    private List<String> images;
    private LocalDateTime updatedAt;
    private String authorName;
    private List<String> taggedMemberNames;
    private String category;

    @Builder
    public BlogDetailResponse(Long postId, String title, String contentHtml, List<String> images, LocalDateTime updatedAt, String authorName, List<String> taggedMemberNames, String category) {
        this.postId = postId;
        this.title = title;
        this.contentHtml = contentHtml;
        this.images = images;
        this.updatedAt = updatedAt;
        this.authorName = authorName;
        this.taggedMemberNames = taggedMemberNames;
        this.category = category;
    }

    public static BlogDetailResponse from(BlogPost draft) {
        return BlogDetailResponse.builder()
                .postId(draft.getId())
                .title(draft.getTitle())
                .contentHtml(draft.getContent())
                .images(draft.getImages().stream()
                        .map(BlogImage::getUrl)
                        .toList())
                .updatedAt(draft.getUpdatedAt())
                .authorName(draft.getAuthor().getUsername())
                .taggedMemberNames(draft.getTaggedMembers().stream()
                        .map(User::getUsername)
                        .toList())
                .category(draft.getCategory().name())
                .build();
    }
}
