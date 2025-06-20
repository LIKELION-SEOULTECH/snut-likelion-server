package com.snut_likelion.domain.blog.dto;

import com.snut_likelion.domain.blog.entity.BlogPost;
import com.snut_likelion.domain.blog.entity.Category;
import com.snut_likelion.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@NoArgsConstructor
public class CreateBlogRequest {

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

    public BlogPost toEntity(User author) {
        return BlogPost.builder()
                .title(title)
                .content(content)
                .category(category)
                .author(author)
                .build();
    }
}
