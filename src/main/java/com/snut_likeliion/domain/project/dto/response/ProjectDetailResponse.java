package com.snut_likeliion.domain.project.dto.response;

import com.snut_likeliion.domain.project.entity.Project;
import com.snut_likeliion.domain.project.entity.ProjectCategory;
import com.snut_likeliion.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectDetailResponse {

    private Long id;
    private String name;
    private String intro;
    private String description;
    private int generation;
    private String websiteUrl;
    private String playstoreUrl;
    private String appstoreUrl;
    private List<String> tags;
    private List<Participant> members;
    private ProjectCategory category;
    private List<String> imageUrls;

    @Builder
    public ProjectDetailResponse(Long id, String name, String intro, String description, int generation, String websiteUrl, String playstoreUrl, String appstoreUrl, List<String> tags, List<Participant> members, ProjectCategory category, List<String> imageUrls) {
        this.id = id;
        this.name = name;
        this.intro = intro;
        this.description = description;
        this.generation = generation;
        this.websiteUrl = websiteUrl;
        this.playstoreUrl = playstoreUrl;
        this.appstoreUrl = appstoreUrl;
        this.tags = tags;
        this.members = members;
        this.category = category;
        this.imageUrls = imageUrls;
    }

    public static ProjectDetailResponse from(Project project) {
        return ProjectDetailResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .intro(project.getIntro())
                .description(project.getDescription())
                .generation(project.getGeneration())
                .websiteUrl(project.getWebsiteUrl())
                .playstoreUrl(project.getPlaystoreUrl())
                .appstoreUrl(project.getAppstoreUrl())
                .category(project.getCategory())
                .tags(project.getTagList())
                .members(
                        project.getParticipations().stream()
                                .map(projectParticipation -> {
                                    User member = projectParticipation.getLionInfo().getUser(); // TODO: N+1
                                    return Participant.from(member);
                                })
                                .toList()
                ) // TODO: N+1
                .imageUrls(project.getImageUrlList())
                .build();
    }

    @Getter
    public static class Participant {
        private Long id;
        private String username;

        @Builder
        public Participant(Long id, String username) {
            this.id = id;
            this.username = username;
        }

        public static Participant from(User user) {
            return Participant.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .build();
        }
    }

}
