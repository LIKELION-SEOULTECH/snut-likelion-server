package com.snut_likelion.domain.recruitment.infra;

import com.snut_likelion.domain.recruitment.entity.Recruitment;
import com.snut_likelion.domain.recruitment.entity.RecruitmentType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RecruitmentRepository extends JpaRepository<Recruitment, Long> {
    @Query("SELECT r FROM Recruitment r WHERE r.recruitmentType = :recruitmentType " +
            "ORDER BY r.openDate DESC LIMIT 1")
    Optional<Recruitment> findRecentRecruitment(RecruitmentType recruitmentType);

    @EntityGraph(attributePaths = {"questions"})
    Optional<Recruitment> findWithQuestionsById(Long recId);
}
