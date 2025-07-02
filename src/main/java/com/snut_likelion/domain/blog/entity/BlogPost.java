package com.snut_likelion.domain.blog.entity;

import com.snut_likelion.domain.user.entity.User;
import com.snut_likelion.global.support.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "blog_post")
public class BlogPost extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;  // HTML

    // 대표 썸네일 URL
    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostStatus status = PostStatus.PUBLISHED; // 기본 = PUBLISHED

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id")
    private User author;

    @OneToMany(
            mappedBy = "post",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<BlogImage> images = new ArrayList<>();

    // 사람 태그
    @ManyToMany
    @JoinTable(
            name = "blog_post_tag",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> taggedMembers = new HashSet<>();

    @Builder
    private BlogPost(String title,
                     String content,
                     Category category,
                     Set<User> taggedMembers,
                     PostStatus status) {

        this.title = title;
        this.content = content;
        this.category = category;

        if (taggedMembers != null) {
            this.taggedMembers.addAll(taggedMembers);
        }

        if (status != null) {
            this.status = status;
        }
    }

    public void setStatus(PostStatus status) {
        this.status = status;
    }  //  글 상태 변경 (임시저장 <-> 게시)

    public void updatePost(String title, String content, Category category) {
        if (StringUtils.hasText(title)) this.title = title;
        if (StringUtils.hasText(content)) this.content = content;
        if (category != null) this.category = category;
    }

    // === 연관관계 메서드 ===
    public void setAuthor(User author) {
        this.author = author;
    }

    public void setImages(List<BlogImage> images) {
        this.images.clear();

        if (images == null || images.isEmpty()) {
            this.thumbnailUrl = null;
            return;
        }

        for (BlogImage img : images) {
            img.setPost(this);
        }

        this.images.addAll(images);
        this.thumbnailUrl = images.get(0).getUrl();
    }

    public void setTaggedMembers(Set<User> taggedMembers) {
        this.taggedMembers.clear();
        this.taggedMembers.addAll(taggedMembers);
    }
}