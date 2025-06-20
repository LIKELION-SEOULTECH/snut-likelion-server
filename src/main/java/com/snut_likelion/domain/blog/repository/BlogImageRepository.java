package com.snut_likelion.domain.blog.repository;

import com.snut_likelion.domain.blog.entity.BlogImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogImageRepository extends JpaRepository<BlogImage, Long> {
}