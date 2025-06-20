package com.snut_likelion.domain.project.dto.request;

import com.snut_likelion.domain.project.entity.Project;
import com.snut_likelion.domain.project.entity.ProjectCategory;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
public class CreateProjectRequest {

    @NotEmpty(message = "프로젝트 이름을 입력해주세요.")
    private String name;

    @NotEmpty(message = "프로젝트 한 줄 소개를 입력해주세요.")
    private String intro;

    @NotEmpty(message = "프로젝트 설명을 입력해주세요.")
    private String description;

    @NotNull(message = "프로젝트 기수를 입력해주세요.")
    private int generation;

    @NotNull(message = "프로젝트 카테고리를 선택해주세요.")
    private ProjectCategory category;

    private String websiteUrl;

    private String playstoreUrl;

    private String appstoreUrl;

    private List<RetrospectionDto> retrospections;

    private List<String> tags;

    private List<MultipartFile> images;

    private List<Long> memberIds;

    @Builder
    public CreateProjectRequest(String name, String intro, String description, int generation, String websiteUrl, String playstoreUrl, String appstoreUrl, ProjectCategory category, List<String> tags, List<MultipartFile> images, List<Long> memberIds, List<RetrospectionDto> retrospections) {
        this.name = name;
        this.intro = intro;
        this.description = description;
        this.generation = generation;
        this.websiteUrl = websiteUrl;
        this.playstoreUrl = playstoreUrl;
        this.appstoreUrl = appstoreUrl;
        this.category = category;
        this.tags = tags;
        this.images = images;
        this.memberIds = memberIds;
        this.retrospections = retrospections;
    }

    @AssertTrue(message = "retrospections의 memberId들은 memberIds에 포함되어야 합니다.")
    public boolean isRetrospectionsMemberIdsValid() {
        if (retrospections == null || retrospections.isEmpty()) {
            return true;
        }

        if (memberIds == null || memberIds.isEmpty()) {
            return false;
        }

        return retrospections.stream().allMatch(retrospection -> memberIds.contains(retrospection.getMemberId()));
    }

    public Project toEntityWithValue() {
        return Project.builder()
                .name(name)
                .intro(intro)
                .description(description)
                .generation(generation)
                .websiteUrl(websiteUrl)
                .playstoreUrl(playstoreUrl)
                .appstoreUrl(appstoreUrl)
                .category(category)
                .build();
    }
}
