package com.snut_likelion.domain.project.infra;

import com.snut_likelion.domain.project.entity.ProjectParticipation;
import com.snut_likelion.domain.user.entity.LionInfo;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProjectParticipationRepository extends JpaRepository<ProjectParticipation, Long> {
    @EntityGraph(attributePaths = {"project", "project.images"})
    List<ProjectParticipation> findAllWithProjectByLionInfo(LionInfo lionInfo);

    @Query("SELECT COUNT(pp) > 0 FROM ProjectParticipation pp " +
            "WHERE pp.project.id = :projectId AND pp.lionInfo.id IN :lionInfoIds")
    boolean existsByProject_IdAndLionInfo_Ids(Long projectId, List<Long> lionInfoIds);

    Optional<ProjectParticipation> findByLionInfo_IdAndProject_Id(Long lionInfoId, Long projectId);
}
