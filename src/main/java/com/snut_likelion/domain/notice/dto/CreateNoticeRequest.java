package com.snut_likelion.domain.notice.dto;


import com.snut_likelion.domain.notice.entity.Notice;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreateNoticeRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String content;

    private Boolean pinned = false;  // 기본값 false

    public Notice toEntity(){

        return Notice.builder()
                .title(title)
                .content(content)
                .pinned(pinned)
                .build();
    }
}