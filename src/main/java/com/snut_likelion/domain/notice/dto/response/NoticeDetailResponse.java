package com.snut_likelion.domain.notice.dto.response;

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
    private String summary;
    private LocalDateTime updatedAt;

    public static NoticeDetailResponse from(Notice notice) {

        return NoticeDetailResponse.builder()
                .noticeId(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .summary(notice.getSummary())
                .updatedAt(notice.getUpdatedAt())
                .build();
    }
}