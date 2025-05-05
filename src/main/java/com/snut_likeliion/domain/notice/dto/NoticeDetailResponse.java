package com.snut_likeliion.domain.notice.dto;

import com.snut_likeliion.domain.notice.entity.Notice;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Getter
@Builder
public class NoticeDetailResponse {

    private Long noticeId;
    private String title;
    private String content;
    // private Boolean pinned;  -> 필요 시 활성화
    private String updatedAt;

    public static NoticeDetailResponse from(Notice notice) {

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String updated = notice.getUpdatedAt() != null
                ? notice.getUpdatedAt().format(fmt)
                : notice.getCreatedAt().format(fmt);

        return NoticeDetailResponse.builder()
                .noticeId(notice.getId())
                .title(notice.getTitle())
                .content(notice.getTitle())
                .updatedAt(notice.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .build();
    }
}
