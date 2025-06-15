package com.snut_likeliion.domain.project.dto.response;

import com.snut_likeliion.domain.project.entity.ProjectRetrospection;
import com.snut_likeliion.domain.user.entity.LionInfo;
import com.snut_likeliion.domain.user.entity.Part;
import com.snut_likeliion.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RetrospectionResponse {

    private Long id;
    private String content;
    private Writer writer;

    @Builder
    public RetrospectionResponse(Long id, String content, Writer writer) {
        this.id = id;
        this.content = content;
        this.writer = writer;
    }

    public static RetrospectionResponse from(ProjectRetrospection projectRetrospection) {
        return RetrospectionResponse.builder()
                .id(projectRetrospection.getId())
                .content(projectRetrospection.getContent())
                .writer(Writer.from(projectRetrospection.getWriter()))
                .build();
    }

    @Getter
    public static class Writer {
        private Long id;
        private String name;
        private Part part;

        @Builder
        public Writer(Long id, String name, Part part) {
            this.id = id;
            this.name = name;
            this.part = part;
        }

        public static Writer from(User writer) {
            LionInfo currentLionInfo = writer.getLionInfos().get(0);
            return Writer.builder()
                    .id(writer.getId())
                    .name(writer.getUsername())
                    .part(currentLionInfo.getPart())
                    .build();
        }
    }
}
