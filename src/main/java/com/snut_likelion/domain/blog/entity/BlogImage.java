package com.snut_likelion.domain.blog.entity;

import com.snut_likelion.global.support.BaseEntity;
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

    @Builder
    public BlogImage(String url) { this.url = url; }

    public void setPost(BlogPost post) { this.post = post; }
}