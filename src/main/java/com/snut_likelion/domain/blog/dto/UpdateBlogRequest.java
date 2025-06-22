package com.snut_likelion.domain.blog.dto;

import com.snut_likelion.domain.blog.entity.BlogPost;
import com.snut_likelion.domain.blog.entity.Category;
import com.snut_likelion.domain.blog.entity.PostStatus;
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

    public void setTitle(String title)                     { this.title = title; }
    public void setContentHtml(String contentHtml)         { this.contentHtml = contentHtml; }
    public void setCategory(Category category)             { this.category = category; }
    public void setImages(List<MultipartFile> images)      { this.images = images; }
    public void setTaggedMemberIds(List<Long> ids)         { this.taggedMemberIds = ids; }
    public void setStatus(PostStatus status)               { this.status = status; }

    public void applyTo(BlogPost post) {
        // 바뀌는 값만 선택, 나머지는 기존 값 사용
        String   newTitle   = (title != null) ? title : post.getTitle();
        String   newContent = (contentHtml != null) ? contentHtml : post.getContent();
        Category newCat     = (category != null) ? category : post.getCategory();

        // 세 필드 중 하나라도 변경 요청이 있으면 updatePost() 호출
        if (title != null || contentHtml != null || category != null) {
            post.updatePost(newTitle, newContent, newCat);
        }

        // 상태 변경
        if (status != null) {
            post.changeStatus(status);
        }

        /* 이미지·태그는 서비스 레이어에서 별도 처리 */
    }
}
