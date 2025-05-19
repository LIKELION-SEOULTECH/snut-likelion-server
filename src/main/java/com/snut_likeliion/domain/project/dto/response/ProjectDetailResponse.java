package com.snut_likeliion.domain.project.dto.response;

import com.snut_likeliion.domain.project.entity.ProjectCategory;
import lombok.AccessLevel;
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
    private List<String> images;

    public class Participant {
        private Long id;
        private String username;
    }
}
