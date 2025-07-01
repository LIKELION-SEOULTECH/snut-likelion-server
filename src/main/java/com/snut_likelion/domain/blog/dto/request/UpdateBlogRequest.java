package com.snut_likelion.domain.blog.dto.request;

import com.snut_likelion.domain.blog.entity.Category;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class UpdateBlogRequest {

    private String title;
    private String contentHtml;
    private Category category;
    private List<String> newImages; // 새롭게 추가된 이미지들
    private List<Long> taggedMemberIds;

    @Builder
    public UpdateBlogRequest(String title, String contentHtml, Category category, List<String> newImages, List<Long> taggedMemberIds) {
        this.title = title;
        this.contentHtml = contentHtml;
        this.category = category;
        this.newImages = newImages;
        this.taggedMemberIds = taggedMemberIds;
    }
}
