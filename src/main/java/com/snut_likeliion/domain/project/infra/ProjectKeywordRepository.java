package com.snut_likeliion.domain.project.infra;

import com.snut_likeliion.domain.project.entity.ProjectKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectKeywordRepository extends JpaRepository<ProjectKeyword, Long> {
}
