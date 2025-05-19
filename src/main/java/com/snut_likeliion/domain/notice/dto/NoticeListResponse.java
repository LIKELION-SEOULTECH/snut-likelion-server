package com.snut_likeliion.domain.notice.dto;

import com.snut_likeliion.domain.notice.entity.Notice;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NoticeListResponse {

    private Long noticeId;
    private String title;
    private Boolean pinned;
    private LocalDateTime updatedAt;

    @Builder
    public NoticeListResponse(Long noticeId, String title, Boolean pinned, LocalDateTime updatedAt) {
        this.noticeId = noticeId;
        this.title = title;
        this.pinned = pinned;
        this.updatedAt = updatedAt;
    }

    public static NoticeListResponse from(Notice notice) {

        return NoticeListResponse.builder()
                .noticeId(notice.getId())
                .title(notice.getTitle())
                .pinned(notice.getPinned())
                .updatedAt(notice.getUpdatedAt())
                .build();
    }
}