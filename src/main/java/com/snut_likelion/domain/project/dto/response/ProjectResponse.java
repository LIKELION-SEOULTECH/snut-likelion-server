package com.snut_likelion.domain.project.dto.response;

import com.snut_likelion.domain.project.entity.Project;
import com.snut_likelion.domain.project.entity.ProjectCategory;
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
    private List<String> tags;
    private ProjectCategory category;
    private String thumbnailUrl;

    @Builder
    public ProjectResponse(Long id, String name, String description, int generation, List<String> tags, ProjectCategory category, String thumbnailUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.generation = generation;
        this.tags = tags;
        this.category = category;
        this.thumbnailUrl = thumbnailUrl;
    }

    public static ProjectResponse from(Project project) {
        return ProjectResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .generation(project.getGeneration())
                .tags(project.getTagList())
                .category(project.getCategory())
                .thumbnailUrl(project.getThumbnailUrl())
                .build();
    }
}
