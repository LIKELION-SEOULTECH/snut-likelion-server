package com.snut_likelion.domain.blog.dto;

import java.time.LocalDateTime;

//  블로그 목록 조회 DTO -> 제목, 수정일, 썸네일만 전달
public record BlogSummaryResponse(
        Long postId,
        String title,
        LocalDateTime updatedAt,
        String thumbnailUrl
) { }
