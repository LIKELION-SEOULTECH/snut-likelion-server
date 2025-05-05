package com.snut_likeliion.domain.notice.dto;

import com.snut_likeliion.domain.notice.entity.Notice;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Getter
@Builder
public class NoticeListResponse {

    private Long noticeId;
    private String title;
    private Boolean pinned;
    private String updatedAt;

    public static NoticeListResponse from(Notice notice) {
        return NoticeListResponse.builder()
                .noticeId(notice.getId())
                .title(notice.getTitle())
                .pinned(notice.getPinned())
                .updatedAt(notice.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .build();
    }
}
