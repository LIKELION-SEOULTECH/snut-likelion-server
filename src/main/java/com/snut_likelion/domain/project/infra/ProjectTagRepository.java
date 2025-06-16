package com.snut_likelion.domain.project.infra;

import com.snut_likelion.domain.project.entity.ProjectTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectTagRepository extends JpaRepository<ProjectTag, Long> {
}
