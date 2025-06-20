package com.snut_likelion.domain.project.infra;

import com.snut_likelion.domain.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
