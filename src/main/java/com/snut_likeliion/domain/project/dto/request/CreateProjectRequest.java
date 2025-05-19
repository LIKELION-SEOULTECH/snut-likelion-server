package com.snut_likeliion.domain.project.dto.request;

import com.snut_likeliion.domain.project.entity.ProjectCategory;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
    private List<String> keywords;
    @NotNull(message = "프로젝트 카테고리를 선택해주세요.")
    private ProjectCategory category;
    private List<MultipartFile> images;
    private List<Long> members;
}
