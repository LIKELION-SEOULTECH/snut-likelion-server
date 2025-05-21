package com.snut_likeliion.domain.project.dto.request;

import com.snut_likeliion.domain.project.entity.ProjectCategory;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
public class UpdateProjectRequest {

    private String name;
    private String intro;
    private String description;
    private Integer generation;
    private List<String> keywords;
    private ProjectCategory category;
    private List<MultipartFile> newImages; // 추가할 이미지
    private List<Long> members;

    @Builder
    public UpdateProjectRequest(String name, String intro, String description, Integer generation, List<String> keywords, ProjectCategory category, List<MultipartFile> newImages, List<Long> members) {
        this.name = name;
        this.intro = intro;
        this.description = description;
        this.generation = generation;
        this.keywords = keywords;
        this.category = category;
        this.newImages = newImages;
        this.members = members;
    }
}
