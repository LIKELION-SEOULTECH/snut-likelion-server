package com.snut_likeliion.domain.blog.dto;

import com.snut_likeliion.domain.blog.entity.BlogPost;
import com.snut_likeliion.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
public class CreateBlogRequest {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "본문을 입력해주세요.")
    private String content;

    private List<MultipartFile> images;

    @NotNull(message = "썸네일 인덱스를 선택해주세요.")  // 인덱스로 선택할지? 선택 안 하면, 첫 번째 사진으로 디폴트할지?,,
    private Integer thumbnailIndex;

    private List<Long> taggedMemberIds;

    public BlogPost toEntity(User author) {
        return BlogPost.createBuilder()
                .title(title)
                .content(content)
                .author(author)
                .build();
    }
}
