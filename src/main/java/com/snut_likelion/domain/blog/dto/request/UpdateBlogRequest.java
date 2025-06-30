package com.snut_likelion.domain.blog.dto.request;

import com.snut_likelion.domain.blog.entity.Category;
import com.snut_likelion.domain.blog.entity.PostStatus;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
public class UpdateBlogRequest {

    private String title;
    private String contentHtml;
    private Category category;
    private List<MultipartFile> images;
    private List<Long> taggedMemberIds;
    private PostStatus status;

    @Builder
    public UpdateBlogRequest(String title, String contentHtml, Category category, List<MultipartFile> images, List<Long> taggedMemberIds, PostStatus status) {
        this.title = title;
        this.contentHtml = contentHtml;
        this.category = category;
        this.images = images;
        this.taggedMemberIds = taggedMemberIds;
        this.status = status;
    }
}
