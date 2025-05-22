package com.snut_likeliion.domain.blog.entity;

import com.snut_likeliion.domain.user.entity.User;
import com.snut_likeliion.global.support.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
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

    @ManyToMany
    @JoinTable(
            name = "blog_post_tag",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> taggedMembers = new HashSet<>();

    public void addTage(User user) {
        taggedMembers.add(user);
    }

    @Builder(builderMethodName = "createBuilder")
    private BlogPost(String title, String content, User author) { // ★ 타입 변경
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public void changeThumbnail(String url) {
        this.thumbnailUrl = url;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}