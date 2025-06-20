package com.snut_likelion.domain.notice.entity;

import com.snut_likelion.global.support.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice extends BaseEntity {

    @Column(nullable = false, length = 100)  // 길이 제한 필요한가??
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private Boolean pinned = false;

    @Builder
    private Notice(String title, String content, Boolean pinned) {
        this.title = title;
        this.content = content;
        this.pinned = pinned != null && pinned;
    }

    public void update(String title, String content, Boolean pinned) {
        this.title = title;
        this.content = content;
        this.pinned = pinned;
    }
}