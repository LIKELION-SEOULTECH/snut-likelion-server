package com.snut_likelion.domain.blog.entity;

import com.snut_likelion.domain.user.entity.User;
import com.snut_likelion.global.support.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
                     User author,
                     List<BlogImage> images,
                     Set<User> taggedMembers,
                     PostStatus status) {

        this.title = title;
        this.content = content;
        this.category = category;
        this.author = author;

        if (images != null && !images.isEmpty()) {
            for (BlogImage img : images) {
                img.setPost(this);
            }
            this.images.addAll(images);
            this.thumbnailUrl = images.get(0).getUrl();
        }

        if (taggedMembers != null) {
            this.taggedMembers.addAll(taggedMembers);
        }

        if (status != null) {
            this.status = status;
        }
    }

    public void changeStatus(PostStatus status) { this.status = status; }  //  글 상태 변경 (임시저장 <-> 게시)

    public void changeThumbnail(String url) {
        this.thumbnailUrl = url;
    }

    public void addTag(User user) { this.taggedMembers.add(user); }
    public void removeTag(User user) { this.taggedMembers.remove(user); }
    public void replaceTags(Set<User> newTags) {  // 태그 일괄 교체
        this.taggedMembers.clear();
        this.taggedMembers.addAll(newTags);
    }

    public void updatePost(String title, String content, Category category) {
        this.title = title;
        this.content = content;
        this.category = category;
    }
}