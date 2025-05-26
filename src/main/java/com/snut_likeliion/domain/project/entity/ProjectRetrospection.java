package com.snut_likeliion.domain.project.entity;

import com.snut_likeliion.domain.user.entity.User;
import com.snut_likeliion.global.support.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "project_retrospection")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectRetrospection extends BaseEntity {

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", referencedColumnName = "id")
    private User writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    private Project project;

    @Builder
    public ProjectRetrospection(String content, User writer, Project project) {
        this.content = content;
        this.writer = writer;
        this.project = project;
    }

    public static ProjectRetrospection of(String content, User writer, Project project) {
        return ProjectRetrospection.builder()
                .content(content)
                .writer(writer)
                .project(project)
                .build();
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
