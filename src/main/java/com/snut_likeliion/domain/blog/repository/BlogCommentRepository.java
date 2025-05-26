package com.snut_likeliion.domain.blog.repository;

import com.snut_likeliion.domain.blog.entity.BlogComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogCommentRepository extends JpaRepository<BlogComment, Long> {
}
