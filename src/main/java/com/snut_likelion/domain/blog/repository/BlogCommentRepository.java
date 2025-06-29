package com.snut_likelion.domain.blog.repository;

import com.snut_likelion.domain.blog.entity.BlogComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogCommentRepository extends JpaRepository<BlogComment, Long> {
}
