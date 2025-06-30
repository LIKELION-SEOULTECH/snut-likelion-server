package com.snut_likelion.domain.blog.dto;

import com.snut_likelion.domain.blog.entity.BlogPost;
import com.snut_likelion.domain.blog.entity.Category;
import com.snut_likelion.domain.blog.entity.PostStatus;
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
    private String contentHtml;

    @NotNull(message = "카테고리를 선택해주세요.")
    private Category category;

    private List<MultipartFile> images;

    private List<Long> taggedMemberIds;

    // 임시저장 / 게시 (기본 = PUBLISHED)
    private PostStatus status = PostStatus.PUBLISHED;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContentHtml(String contentHtml) {
        this.contentHtml = contentHtml;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setImages(List<MultipartFile> images) {
        this.images = images;
    }

    public void setTaggedMemberIds(List<Long> ids) {
        this.taggedMemberIds = ids;
    }

    public void setStatus(PostStatus status) {
        this.status = status;
    }

    public BlogPost toEntity() {
        return BlogPost.builder()
                .title(title)
                .content(contentHtml)
                .category(category)
                .status(status)
                .build();
    }
}
