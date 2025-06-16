package com.snut_likelion.domain.project.infra;

import com.snut_likelion.domain.project.entity.ProjectRetrospection;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRetrospectionRepository extends JpaRepository<ProjectRetrospection, Long> {

    @EntityGraph(attributePaths = {"writer", "project"})
    List<ProjectRetrospection> findByProject_Id(Long projectId);

    Optional<ProjectRetrospection> findByWriter_IdAndProject_Id(Long writerId, Long projectId);
}
