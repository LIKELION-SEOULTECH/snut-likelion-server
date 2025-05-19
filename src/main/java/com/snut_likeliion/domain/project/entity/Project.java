package com.snut_likeliion.domain.project.entity;

import com.snut_likeliion.global.support.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private List<ProjectParticipation> members = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<ProjectKeyword> keywords = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<ProjectImage> images = new ArrayList<>();


}
