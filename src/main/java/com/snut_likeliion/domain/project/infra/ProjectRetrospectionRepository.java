package com.snut_likeliion.domain.project.infra;

import com.snut_likeliion.domain.project.entity.ProjectRetrospection;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRetrospectionRepository extends JpaRepository<ProjectRetrospection, Long> {

    @EntityGraph(attributePaths = {"writer"})
    List<ProjectRetrospection> findByProject_Id(Long projectId);
}
