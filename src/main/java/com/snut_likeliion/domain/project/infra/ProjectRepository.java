package com.snut_likeliion.domain.project.infra;

import com.snut_likeliion.domain.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
