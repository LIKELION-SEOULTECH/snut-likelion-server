package com.snut_likeliion.domain.project.entity;

import com.snut_likeliion.global.support.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Table(name = "projects")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project extends BaseEntity {

    private String name;

    private String intro;

    private String description;

    private int generation;

    @Enumerated(EnumType.STRING)
    private ProjectCategory category;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<ProjectParticipation> participations = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<ProjectKeyword> keywords = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<ProjectImage> images = new ArrayList<>();

    @Builder
    public Project(Long id, String name, String intro, String description, int generation, ProjectCategory category) {
        this.id = id;
        this.name = name;
        this.intro = intro;
        this.description = description;
        this.generation = generation;
        this.category = category;
    }

    public void addImage(ProjectImage projectImage) {
        this.images.add(projectImage);
        projectImage.setProject(this);
    }

    public void setKeywords(List<ProjectKeyword> projectKeywords) {
        this.keywords = projectKeywords;
        projectKeywords.forEach(projectKeyword -> projectKeyword.setProject(this));
    }

    public void setParticipants(List<ProjectParticipation> participations) {
        this.participations = participations;
    }

    public void update(String name, String intro, String description, Integer generation, ProjectCategory category) {
        if (StringUtils.hasText(name)) this.name = name;
        if (StringUtils.hasText(intro)) this.intro = intro;
        if (StringUtils.hasText(description)) this.description = description;
        if (generation != null) this.generation = generation;
        if (category != null) this.category = category;
    }
}
