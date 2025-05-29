package com.snut_likeliion.domain.blog.dto;

import com.snut_likeliion.domain.blog.entity.BlogPost;
import com.snut_likeliion.domain.blog.entity.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
public class UpdateBlogRequest {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "본문을 입력해주세요.")
    private String content;

    @NotNull(message = "카테고리를 선택해주세요.")
    private Category category;

    private List<MultipartFile> images;

    private Integer thumbnailIndex;

    private List<Long> taggedMemberIds;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setImages(List<MultipartFile> images) {
        this.images = images;
    }

    public void setThumbnailIndex(Integer thumbnailIndex) {
        this.thumbnailIndex = thumbnailIndex;
    }

    public void setTaggedMemberIds(List<Long> taggedMemberIds) {
        this.taggedMemberIds = taggedMemberIds;
    }

    public void applyTo(BlogPost post) {
        post.update(title, content, category);
    }
}
