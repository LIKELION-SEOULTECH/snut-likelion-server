package com.snut_likelion.domain.project.entity;

import com.snut_likelion.global.support.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "project_images")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectImage extends BaseEntity {

    @Column(nullable = false)
    private String originalName;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String storedName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    private Project project;

    public void setProject(Project project) {
        this.project = project;
    }

    @Builder
    public ProjectImage(String originalName, String storedName) {
        this.originalName = originalName;
        this.storedName = storedName;
    }

    public static ProjectImage of(String originalName, String storedName) {
        return ProjectImage.builder()
                .originalName(originalName)
                .storedName(storedName)
                .build();
    }
}
