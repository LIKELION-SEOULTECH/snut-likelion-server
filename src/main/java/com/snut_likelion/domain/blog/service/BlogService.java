package com.snut_likelion.domain.blog.service;

import com.snut_likelion.domain.blog.dto.*;
import com.snut_likelion.domain.blog.entity.*;
import com.snut_likelion.domain.blog.exception.BlogErrorCode;
import com.snut_likelion.domain.blog.repository.BlogPostRepository;
import com.snut_likelion.global.provider.FileProvider;
import com.snut_likelion.domain.user.entity.User;
import com.snut_likelion.domain.user.exception.UserErrorCode;
import com.snut_likelion.domain.user.repository.UserRepository;
import com.snut_likelion.global.auth.model.UserInfo;
import com.snut_likelion.global.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlogService {

    private final BlogPostRepository postRepo;
    private final UserRepository     userRepo;
    private final FileProvider       fileProvider;
    private final BlogQueryService   queryService;

    @Lazy @Autowired
    private BlogService self;

    public Long createPost(CreateBlogRequest req, UserInfo info) { return self.createPost(req, toUser(info)); }
    public Long updatePost(Long id, UpdateBlogRequest req, UserInfo info) { return self.updatePost(id, req, toUser(info)); }
    public void deletePost(Long id, UserInfo info) { self.deletePost(id, toUser(info)); }

    public Long saveDraft(CreateBlogRequest req, UserInfo info) { return self.saveDraft(req, toUser(info)); }
    public BlogDetailResponse loadDraft(UserInfo info) { return self.loadDraft(toUser(info)); }
    public void discardDraft(UserInfo info) { self.discardDraft(toUser(info)); }

    @Transactional
    @PreAuthorize("@authChecker.checkIsOfficialAndManager(#req.category, principal)")
    public Long createPost(CreateBlogRequest req, User author) {

        BlogPost post = BlogPost.builder()
                .title(req.getTitle())
                .content(req.getContentHtml())
                .category(req.getCategory())
                .author(author)
                .images(uploadImages(req.getImages()))
                .taggedMembers(fetchUsers(req.getTaggedMemberIds()))
                .status(Optional.ofNullable(req.getStatus())
                        .orElse(PostStatus.PUBLISHED))
                .build();

        return postRepo.save(post).getId();
    }

    @Transactional
    @PreAuthorize("@authChecker.checkCanModify(#postId, principal)")
    public Long updatePost(Long postId, UpdateBlogRequest req, User editor) {

        BlogPost post = postRepo.findById(postId)
                .orElseThrow(() -> new NotFoundException(BlogErrorCode.POST_NOT_FOUND));

        // 기본 텍스트, 카테고리
        req.applyTo(post);

        // 태그 교체
        if (req.getTaggedMemberIds() != null) {
            post.replaceTags(fetchUsers(req.getTaggedMemberIds()));
        }

        // 이미지 교체
        if (req.getImages() != null && !req.getImages().isEmpty()) {
            post.getImages().clear();                            // orphanRemoval
            List<BlogImage> imgs = uploadImages(req.getImages());
            imgs.forEach(i -> i.setPost(post));
            post.getImages().addAll(imgs);
            post.changeThumbnail(imgs.get(0).getUrl());
        }
        return post.getId();
    }

    @Transactional
    @PreAuthorize("@authChecker.checkCanModify(#postId, principal)")
    public void deletePost(Long postId, User editor) {

        BlogPost post = postRepo.findById(postId)
                .orElseThrow(() -> new NotFoundException(BlogErrorCode.POST_NOT_FOUND));

        // S3(또는 로컬)에 저장된 이미지 삭제
        post.getImages().forEach(img ->
                fileProvider.deleteFile(img.getUrl().replace("/images/", "")));

        postRepo.delete(post);
    }

    // 임시저장 / 덮어쓰기
    @Transactional
    public Long saveDraft(CreateBlogRequest req, User author) {

        Optional<BlogPost> draftOpt =
                postRepo.findByAuthorAndStatus(author, PostStatus.DRAFT);

        if (draftOpt.isPresent()) {  // 덮어쓰기
            BlogPost draft = draftOpt.get();
            draft.updatePost(req.getTitle(), req.getContentHtml(), req.getCategory());
            draft.replaceTags(fetchUsers(req.getTaggedMemberIds()));

            if (req.getImages() != null && !req.getImages().isEmpty()) {
                draft.getImages().clear();
                List<BlogImage> imgs = uploadImages(req.getImages());
                imgs.forEach(i -> i.setPost(draft));
                draft.getImages().addAll(imgs);
                draft.changeThumbnail(imgs.get(0).getUrl());
            }
            return draft.getId();
        }

        // 새로 생성
        BlogPost newDraft = BlogPost.builder()
                .title(req.getTitle())
                .content(req.getContentHtml())
                .category(req.getCategory())
                .author(author)
                .images(uploadImages(req.getImages()))
                .taggedMembers(fetchUsers(req.getTaggedMemberIds()))
                .status(PostStatus.DRAFT)
                .build();

        return postRepo.save(newDraft).getId();
    }

    // 불러오기
    @Transactional(readOnly = true)
    public BlogDetailResponse loadDraft(User author) {
        BlogPost draft = postRepo.findByAuthorAndStatus(author, PostStatus.DRAFT)
                .orElseThrow(() -> new NotFoundException(BlogErrorCode.DRAFT_NOT_FOUND));
        return queryService.toDetailDto(draft);
    }

    // 버리기
    @Transactional
    public void discardDraft(User author) {
        postRepo.deleteByAuthorAndStatus(author, PostStatus.DRAFT);
    }

    private User toUser(UserInfo info) {
        return userRepo.findById(info.getId())
                .orElseThrow(() -> new NotFoundException(UserErrorCode.NOT_FOUND));
    }

    private List<BlogImage> uploadImages(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) return List.of();
        List<BlogImage> list = new ArrayList<>();
        for (MultipartFile f : files) {
            String stored = fileProvider.storeFile(f);
            list.add(BlogImage.builder().url("/images/" + stored).build());
        }
        return list;
    }

    private Set<User> fetchUsers(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return Set.of();
        return userRepo.findAllById(ids).stream()
                .collect(Collectors.toSet());
    }
}
