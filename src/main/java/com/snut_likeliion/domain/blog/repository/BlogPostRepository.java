package com.snut_likeliion.domain.blog.repository;

import com.snut_likeliion.domain.blog.entity.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BlogPostRepository extends JpaRepository<BlogPost, Long>, JpaSpecificationExecutor<BlogPost> {
}