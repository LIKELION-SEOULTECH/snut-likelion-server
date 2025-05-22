package com.snut_likeliion.domain.notice.entity;

import com.snut_likeliion.global.support.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

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