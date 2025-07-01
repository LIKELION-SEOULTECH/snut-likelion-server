package com.snut_likelion.domain.blog.service;

import com.snut_likelion.domain.blog.dto.request.CreateBlogRequest;
import com.snut_likelion.domain.blog.dto.request.UpdateBlogRequest;
import com.snut_likelion.domain.blog.entity.BlogImage;
import com.snut_likelion.domain.blog.entity.BlogPost;
import com.snut_likelion.domain.blog.entity.PostStatus;
import com.snut_likelion.domain.blog.exception.BlogErrorCode;
import com.snut_likelion.domain.blog.repository.BlogPostRepository;
import com.snut_likelion.domain.user.entity.User;
import com.snut_likelion.domain.user.exception.UserErrorCode;
import com.snut_likelion.domain.user.repository.UserRepository;
import com.snut_likelion.global.auth.model.UserInfo;
import com.snut_likelion.global.error.exception.NotFoundException;
import com.snut_likelion.global.provider.FileProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlogCommandService {

    private final BlogPostRepository postRepo;
    private final UserRepository userRepo;
    private final FileProvider fileProvider;

    @Transactional
    @PreAuthorize("@authChecker.checkIsOfficialAndManager(#req.category, #author)")
    public Long createPost(CreateBlogRequest req, UserInfo author, boolean submit) {
        BlogPost post = req.toEntity();

        User user = userRepo.findById(author.getId())
                .orElseThrow(() -> new NotFoundException(UserErrorCode.NOT_FOUND));
        post.setAuthor(user);
        post.setImages(this.mappingToBlogImages(req.getImages()));
        post.setTaggedMembers(this.fetchUsers(req.getTaggedMemberIds()));

        if (submit) {
            post.setStatus(PostStatus.PUBLISHED);
        } else {
            post.setStatus(PostStatus.DRAFT);
        }

        return postRepo.save(post).getId();
    }

    @Transactional
    @PreAuthorize("@authChecker.checkCanModify(#postId, #editor)")
    public void updatePost(Long postId, UpdateBlogRequest req, UserInfo editor, boolean submit) {
        BlogPost post = postRepo.findById(postId)
                .orElseThrow(() -> new NotFoundException(BlogErrorCode.POST_NOT_FOUND));

        post.updatePost(
                req.getTitle(),
                req.getContentHtml(),
                req.getCategory()
        );

        if (req.getTaggedMemberIds() != null) {
            post.setTaggedMembers(this.fetchUsers(req.getTaggedMemberIds()));
        }

        if (req.getNewImages() != null) {
            post.setImages(this.mappingToBlogImages(req.getNewImages()));
        }

        if (submit) {
            post.setStatus(PostStatus.PUBLISHED);
        } else {
            post.setStatus(PostStatus.DRAFT);
        }
    }

    @Transactional
    @PreAuthorize("@authChecker.checkCanModify(#postId, #editor)")
    public void deletePost(Long postId, UserInfo editor) {
        BlogPost post = postRepo.findById(postId)
                .orElseThrow(() -> new NotFoundException(BlogErrorCode.POST_NOT_FOUND));

        // S3(또는 로컬)에 저장된 이미지 삭제
        post.getImages()
                .forEach(img -> fileProvider.deleteFile(img.getUrl()));

        postRepo.delete(post);
    }

    // 버리기
    @Transactional
    public void discardDraft(UserInfo author) {
        User user = userRepo.findById(author.getId())
                .orElseThrow(() -> new NotFoundException(UserErrorCode.NOT_FOUND));
        postRepo.deleteByAuthorAndStatus(user, PostStatus.DRAFT);
    }

    private List<BlogImage> mappingToBlogImages(List<String> imageUrls) {
        if (imageUrls == null || imageUrls.isEmpty()) return List.of();
        List<BlogImage> list = new ArrayList<>();
        for (String url : imageUrls) {
            BlogImage blogImage = BlogImage.builder()
                    .url(url)
                    .build();
            list.add(blogImage);
        }
        return list;
    }

    private Set<User> fetchUsers(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return Set.of();
        return userRepo.findAllById(ids).stream()
                .collect(Collectors.toSet());
    }
}
