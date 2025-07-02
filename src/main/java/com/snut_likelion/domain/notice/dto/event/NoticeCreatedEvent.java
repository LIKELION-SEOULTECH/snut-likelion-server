package com.snut_likelion.domain.notice.dto.event;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoticeCreatedEvent {
    private Long noticeId;
    private String content;

    @Builder
    public NoticeCreatedEvent(Long noticeId, String content) {
        this.noticeId = noticeId;
        this.content = content;
    }
}
