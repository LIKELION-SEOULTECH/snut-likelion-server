package com.snut_likelion.domain.blog.repository;

import com.snut_likelion.domain.blog.entity.*;
import com.snut_likelion.domain.user.entity.User;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {

    // 게시(PUBLISHED) 목록
    Page<BlogPost> findByStatusAndCategoryOrderByUpdatedAtDesc(
            PostStatus status,
            Category   category,
            Pageable   pageable
    );

    // 제목 검색
    Page<BlogPost> findByStatusAndCategoryAndTitleContainingIgnoreCaseOrderByUpdatedAtDesc(
            PostStatus status,
            Category   category,
            String     title,     // keyword
            Pageable   pageable
    );

    // 작성자별 임시저장(DRAFT) 단건
    Optional<BlogPost> findByAuthorAndStatus(
            User       author,
            PostStatus status
    );

    // 임시저장 삭제
    void deleteByAuthorAndStatus(
            User       author,
            PostStatus status
    );
}
