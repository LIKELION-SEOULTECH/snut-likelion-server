package com.snut_likelion.domain.notice.dto;

import lombok.Getter;

@Getter
public class UpdateNoticeRequest {
    private String title;
    private String content;
    private Boolean pinned;
}
