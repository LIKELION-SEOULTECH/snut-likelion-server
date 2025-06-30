package com.snut_likelion.domain.blog.service;

import com.snut_likelion.domain.blog.dto.CreateBlogRequest;
import com.snut_likelion.domain.blog.dto.UpdateBlogRequest;
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
import org.springframework.web.multipart.MultipartFile;

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
    public Long createPost(CreateBlogRequest req, UserInfo author) {
        BlogPost post = req.toEntity();

        User user = userRepo.findById(author.getId())
                .orElseThrow(() -> new NotFoundException(UserErrorCode.NOT_FOUND));
        post.setAuthor(user);

        List<BlogImage> imgs = this.uploadImages(req.getImages());
        post.setImages(imgs);

        post.setTaggedMembers(this.fetchUsers(req.getTaggedMemberIds()));

        return postRepo.save(post).getId();
    }

    @Transactional
    @PreAuthorize("@authChecker.checkCanModify(#postId, #editor)")
    public void updatePost(Long postId, UpdateBlogRequest req, UserInfo editor) {
        BlogPost post = postRepo.findById(postId)
                .orElseThrow(() -> new NotFoundException(BlogErrorCode.POST_NOT_FOUND));

        post.updatePost(
                req.getTitle(),
                req.getContentHtml(),
                req.getCategory()
        );

        if (req.getStatus() != null) post.changeStatus(req.getStatus());

        // 태그 교체
        if (req.getTaggedMemberIds() != null) {
            post.setTaggedMembers(this.fetchUsers(req.getTaggedMemberIds()));
        }

        // 이미지 교체
        if (req.getImages() != null) {
            List<BlogImage> blogImages = this.uploadImages(req.getImages());
            post.setImages(blogImages);
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

    // 임시저장 / 덮어쓰기
    @Transactional
    public void saveDraft(CreateBlogRequest req, UserInfo author) {
        User user = userRepo.findById(author.getId())
                .orElseThrow(() -> new NotFoundException(UserErrorCode.NOT_FOUND));

        postRepo.findByAuthorAndStatus(user, PostStatus.DRAFT)
                .ifPresentOrElse(
                        draft -> {
                            draft.updatePost(req.getTitle(), req.getContentHtml(), req.getCategory());
                            draft.setTaggedMembers(this.fetchUsers(req.getTaggedMemberIds()));

                            if (req.getImages() != null) {
                                draft.setImages(this.uploadImages(req.getImages()));
                            }
                        },
                        () -> {
                            BlogPost post = req.toEntity();
                            post.setAuthor(user);
                            post.setImages(this.uploadImages(req.getImages()));
                            post.setTaggedMembers(this.fetchUsers(req.getTaggedMemberIds()));
                            postRepo.save(post);
                        }
                );
    }

    // 버리기
    @Transactional
    public void discardDraft(UserInfo author) {
        User user = userRepo.findById(author.getId())
                .orElseThrow(() -> new NotFoundException(UserErrorCode.NOT_FOUND));
        postRepo.deleteByAuthorAndStatus(user, PostStatus.DRAFT);
    }

    private List<BlogImage> uploadImages(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) return List.of();
        List<BlogImage> list = new ArrayList<>();
        for (MultipartFile f : files) {
            String storedName = fileProvider.storeFile(f);
            BlogImage blogImage = BlogImage.builder()
                    .url(fileProvider.buildImageUrl(storedName))
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
