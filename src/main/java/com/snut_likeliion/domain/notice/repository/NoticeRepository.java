package com.snut_likeliion.domain.notice.repository;

import com.snut_likeliion.domain.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}