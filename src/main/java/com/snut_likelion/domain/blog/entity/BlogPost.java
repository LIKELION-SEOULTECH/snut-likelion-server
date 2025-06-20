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
    private String content;

    // 대표 썸네일 URL
    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id")
    private User author;

    @OneToMany(
            mappedBy = "post",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<BlogImage> images = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "blog_post_tag",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> taggedMembers = new HashSet<>();

    public void addTag(User user) {
        taggedMembers.add(user);
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Builder
    private BlogPost(String title, String content, User author, Category category) { // ★ 타입 변경
        this.title = title;
        this.content = content;
        this.author = author;
        this.category = category;
    }

    public void changeThumbnail(String url) {
        this.thumbnailUrl = url;
    }

    public void update(String title, String content, Category category) {
        this.title = title;
        this.content = content;
        this.category = category;
    }

    public void setUser(User user){
        this.author = user;
    }
}