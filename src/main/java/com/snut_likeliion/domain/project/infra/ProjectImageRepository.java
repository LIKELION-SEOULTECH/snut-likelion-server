package com.snut_likeliion.domain.project.infra;

import com.snut_likeliion.domain.project.entity.ProjectImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectImageRepository extends JpaRepository<ProjectImage, Long> {

    Optional<ProjectImage> findByProject_IdAndOriginalName(Long projectId, String originalName);
}
