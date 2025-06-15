package com.snut_likeliion.domain.project.dto.response;

import com.snut_likeliion.domain.project.entity.Project;
import com.snut_likeliion.domain.project.entity.ProjectCategory;
import com.snut_likeliion.domain.project.entity.ProjectImage;
import com.snut_likeliion.domain.project.entity.ProjectKeyword;
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
    private List<String> keywords;
    private List<Participant> members;
    private ProjectCategory category;
    private List<Image> images;

    @Builder
    public ProjectDetailResponse(Long id, String name, String intro, String description, int generation, List<String> keywords, List<Participant> members, ProjectCategory category, List<Image> images) {
        this.id = id;
        this.name = name;
        this.intro = intro;
        this.description = description;
        this.generation = generation;
        this.keywords = keywords;
        this.members = members;
        this.category = category;
        this.images = images;
    }

    public static ProjectDetailResponse from(Project project) {
        return ProjectDetailResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .intro(project.getIntro())
                .description(project.getDescription())
                .generation(project.getGeneration())
                .category(project.getCategory())
                .keywords(project.getKeywords().stream().map(ProjectKeyword::getName).toList()) // TODO: N+1
                .members(
                        project.getParticipations().stream()
                                .map(projectParticipation -> {
                                    User member = projectParticipation.getLionInfo().getUser(); // TODO: N+1
                                    return Participant.from(member);
                                })
                                .toList()
                ) // TODO: N+1
                .images(project.getImages().stream().map(Image::from).toList()) // TODO: N+1
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

    @Getter
    public static class Image {
        private String originalName;
        private String storedName;

        @Builder
        public Image(String originalName, String storedName) {
            this.originalName = originalName;
            this.storedName = storedName;
        }

        public static Image from(ProjectImage projectImage) {
            return Image.builder()
                    .originalName(projectImage.getOriginalName())
                    .storedName(projectImage.getStoredName())
                    .build();
        }
    }
}
