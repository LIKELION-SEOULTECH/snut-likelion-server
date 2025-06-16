package com.snut_likelion.domain.notice.dto;

import com.snut_likelion.domain.notice.entity.Notice;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NoticeDetailResponse {

    private Long noticeId;
    private String title;
    private String content;
    // private Boolean pinned;  -> 필요 시 활성화
    private LocalDateTime updatedAt;

    public static NoticeDetailResponse from(Notice notice) {

        return NoticeDetailResponse.builder()
                .noticeId(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .updatedAt(notice.getUpdatedAt())
                .build();
    }
}