package com.snut_likeliion.domain.blog.entity;

import com.snut_likeliion.domain.user.entity.User;
import com.snut_likeliion.global.support.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "blog_comment")
public class BlogComment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id")
    private BlogPost post;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private BlogComment parent;

    @Lob
    @Column(nullable = false)
    private String content;

    @Builder(builderMethodName = "createBuilder")
    private BlogComment(BlogPost post, User author, BlogComment parent, String content) {
        this.post = post;
        this.author = author;
        this.parent = parent;
        this.content = content;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}