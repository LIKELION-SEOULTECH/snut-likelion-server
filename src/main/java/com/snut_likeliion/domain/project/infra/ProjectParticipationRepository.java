package com.snut_likeliion.domain.project.infra;

import com.snut_likeliion.domain.project.entity.ProjectParticipation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectParticipationRepository extends JpaRepository<ProjectParticipation, Long> {
}
