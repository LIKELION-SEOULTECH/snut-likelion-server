package com.snut_likeliion.domain.notice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)  // 길이 제한 필요??
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private Boolean pinned;  // 상단 고정 여부 (true: 고정) <- 필요할까??

    @Column(nullable = false)
    private Long viewCount;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public void increaseViewCount() {
        this.viewCount++;
    }

    public void update(String title, String content, Boolean pinned) {
        this.title = title;
        this.content = content;
        this.pinned = pinned;
    }
}

