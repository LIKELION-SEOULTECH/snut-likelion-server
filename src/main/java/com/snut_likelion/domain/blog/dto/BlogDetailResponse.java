package com.snut_likelion.domain.blog.dto;

import java.time.LocalDateTime;
import java.util.List;

// 블로그 단건 조회 DTO -> 썸네일은 제외하고 본문 HTML + 이미지 URL 전체 전달
public record BlogDetailResponse(
        Long postId,
        String title,
        String contentHtml,
        List<String> images,
        LocalDateTime updatedAt,
        String authorName,
        List<String> taggedMemberNames,
        String category
) { }
