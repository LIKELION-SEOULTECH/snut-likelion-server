package com.snut_likeliion.domain.project.dto.response;

import com.snut_likeliion.domain.project.entity.Project;
import com.snut_likeliion.domain.project.entity.ProjectCategory;
import com.snut_likeliion.domain.project.entity.ProjectKeyword;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ProjectResponse {

    private Long id;
    private String name;
    private String description;
    private int generation;
    private List<String> keywords;
    private ProjectCategory category;
    private String representativeImageName;

    @Builder
    public ProjectResponse(Long id, String name, String description, int generation, List<String> keywords, ProjectCategory category, String representativeImageName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.generation = generation;
        this.keywords = keywords;
        this.category = category;
        this.representativeImageName = representativeImageName;
    }

    public static ProjectResponse from(Project project) {
        return ProjectResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .generation(project.getGeneration())
                .keywords(project.getKeywords().stream().map(ProjectKeyword::getName).toList())
                .category(project.getCategory())
                .representativeImageName(
                        project.getImages().isEmpty()
                                ? null
                                : project.getImages().get(0).getStoredName()
                )
                .build();
    }
}
