package com.snut_likelion.domain.blog.dto.request;

import com.snut_likelion.domain.blog.entity.BlogPost;
import com.snut_likelion.domain.blog.entity.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private List<Long> taggedMemberIds;

    private List<String> images;

    @Builder
    public CreateBlogRequest(String title, String contentHtml, Category category, List<String> images, List<Long> taggedMemberIds) {
        this.title = title;
        this.contentHtml = contentHtml;
        this.category = category;
        this.images = images;
        this.taggedMemberIds = taggedMemberIds;
    }

    public BlogPost toEntity() {
        return BlogPost.builder()
                .title(title)
                .content(contentHtml)
                .category(category)
                .build();
    }
}
