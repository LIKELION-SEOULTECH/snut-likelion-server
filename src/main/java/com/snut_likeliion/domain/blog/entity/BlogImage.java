package com.snut_likeliion.domain.blog.entity;

import com.snut_likeliion.global.support.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "blog_image")
public class BlogImage extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id")
    private BlogPost post;

    @Column(nullable = false)
    private String url;

    @Column(name = "is_thumbnail", nullable = false)
    private boolean thumbnail;

    @Builder
    private BlogImage(BlogPost post, String url, boolean thumbnail) {
        this.post = post;
        this.url = url;
        this.thumbnail = thumbnail;
    }

    public void markThumbnail(boolean value) {
        this.thumbnail = value;
    }
}