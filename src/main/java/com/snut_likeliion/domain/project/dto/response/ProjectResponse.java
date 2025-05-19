package com.snut_likeliion.domain.project.dto.response;

import com.snut_likeliion.domain.project.entity.ProjectCategory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectResponse {

    private Long id;
    private String name;
    private String description;
    private int generation;
    private List<String> keywords;
    private ProjectCategory category;
    private String representativeImageUrl;

}
