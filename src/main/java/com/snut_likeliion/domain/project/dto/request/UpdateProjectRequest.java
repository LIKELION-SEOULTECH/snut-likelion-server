package com.snut_likeliion.domain.project.dto.request;

import com.snut_likeliion.domain.project.entity.ProjectCategory;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
public class UpdateProjectRequest {

    private String name;
    private String intro;
    private String description;
    private int generation;
    private List<String> keywords;
    private ProjectCategory category;
    private List<MultipartFile> newImages; // 추가할 이미지
    private List<Long> members;

}
