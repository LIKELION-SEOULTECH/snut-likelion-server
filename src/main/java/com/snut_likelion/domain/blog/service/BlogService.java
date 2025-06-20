package com.snut_likelion.domain.blog.service;

import com.snut_likelion.domain.blog.dto.UpdateBlogRequest;
import com.snut_likelion.domain.blog.entity.BlogImage;
import com.snut_likelion.domain.blog.entity.BlogPost;
import com.snut_likelion.domain.blog.exception.BlogErrorCode;
import com.snut_likelion.domain.blog.repository.BlogImageRepository;
import com.snut_likelion.domain.blog.repository.BlogPostRepository;
import com.snut_likelion.domain.blog.dto.CreateBlogRequest;
import com.snut_likelion.domain.project.infra.FileProvider;
import com.snut_likelion.domain.user.entity.User;
import com.snut_likelion.domain.user.entity.Role;
import com.snut_likelion.domain.user.exception.UserErrorCode;
import com.snut_likelion.domain.user.repository.UserRepository;
import com.snut_likelion.global.auth.model.UserInfo;
import com.snut_likelion.global.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogService {

    private final BlogPostRepository postRepo;
    private final BlogImageRepository imageRepo;
    private final UserRepository userRepo;
    private final FileProvider fileProvider;

    @Transactional
    @PreAuthorize("@authChecker.checkIsOfficialAndManager(#request.category, #loginUserInfo)")
    public Long createPost(CreateBlogRequest request, UserInfo loginUserInfo) {
        // 1. 작성자 조회
        User author = userRepo.findById(loginUserInfo.getId())
                .orElseThrow(() -> new NotFoundException(UserErrorCode.NOT_FOUND));

        // 2. Post 생성 및 저장
        BlogPost post = request.toEntity(author);
        postRepo.save(post);

        // 3. 이미지 업로드 & BlogImage 저장
        List<MultipartFile> images = request.getImages();
        if (images != null && !images.isEmpty()) {
            // 1) 클라이언트가 보낸 값이 널이 아니면 인덱스 그대로, 아니면 0
            int idx = (request.getThumbnailIndex() != null) ? request.getThumbnailIndex() : 0;

            // 2) 음수이거나, 이미지 개수보다 크면 무조건 0으로 리셋
            if (idx < 0 || idx >= images.size()) {
                idx = 0;
            }

            // 3) i == idx 일 때만 대표 썸네일로 표시
            for (int i = 0; i < images.size(); i++) {
                MultipartFile mf = images.get(i);

                // (1) 파일 저장 → 랜덤한 storedFileName 반환
                String storedFileName = fileProvider.storeFile(mf);

                // (2) 외부에 노출할 URL 생성
                String url = "/images/" + storedFileName;

                // (3) BlogImage 엔티티 생성
                BlogImage img = BlogImage.builder()
                        .post(post)
                        .url(url)
                        .thumbnail(i ==  idx)  // 썸네일 인덱스와 일치하면 thumbnail=true
                        .build();

                imageRepo.save(img);

                // (4) 대표 썸네일 URL 설정
                if (i == idx) {
                    post.changeThumbnail(url);
                }
            }
        }

        // 4. 사람 태그 처리
        if (request.getTaggedMemberIds() != null) {
            request.getTaggedMemberIds().forEach(id ->
                    userRepo.findById(id).ifPresent(post::addTag));
        }

        return post.getId();
    }

    @Transactional
    @PreAuthorize("@authChecker.checkCanModify(#postId, #loginUserInfo)")
    public Long updatePost(Long postId, UpdateBlogRequest request, UserInfo loginUserInfo) {
        // 1. 게시글 조회
        BlogPost post = postRepo.findById(postId)
                .orElseThrow(() -> new NotFoundException(BlogErrorCode.POST_NOT_FOUND));

        // 2. 제목/본문/카테고리 수정
        request.applyTo(post);

        // 3. 이미지 수정 (orphanRemoval = true 이용)
        if (request.getImages() != null) {
            // 기존 이미지 모두 제거 → JPA가 DB에서도 자동 삭제
            post.getImages().clear();

            List<MultipartFile> imgs = request.getImages();
            int idx = request.getThumbnailIndex() != null
                    ? request.getThumbnailIndex()
                    : 0;

            for (int i = 0; i < imgs.size(); i++) {
                MultipartFile mf = imgs.get(i);
                String stored = fileProvider.storeFile(mf);
                String url    = "/images/" + stored;

                BlogImage img = BlogImage.builder()
                        .post(post)
                        .url(url)
                        .thumbnail(i == idx)
                        .build();
                post.getImages().add(img);

                if (i == idx) {
                    post.changeThumbnail(url);
                }
            }
        }

        // 4. 태그 업데이트
        if (request.getTaggedMemberIds() != null) {
            post.getTaggedMembers().clear();
            request.getTaggedMemberIds().forEach(id ->
                    userRepo.findById(id).ifPresent(post::addTag)
            );
        }

        return post.getId();
    }

    @Transactional
    @PreAuthorize("@authChecker.checkCanModify(#postId, #loginUserInfo)")
    public void deletePost(Long postId, UserInfo loginUserInfo) {
        // 1. 게시글 조회
        BlogPost post = postRepo.findById(postId)
                .orElseThrow(() -> new NotFoundException(BlogErrorCode.POST_NOT_FOUND));

        // 2. 저장소에서 파일(이미지) 삭제
        for (BlogImage img : post.getImages()) {
            // URL에서 실제 파일명만 떼어내기
            String storedName = img.getUrl().replace("/images/", "");
            fileProvider.deleteFile(storedName);
        }

        // 3. DB에서 이미지 레코드 삭제
        imageRepo.deleteAll(post.getImages());

        // 4. 게시글 삭제
        postRepo.delete(post);
    }
}
