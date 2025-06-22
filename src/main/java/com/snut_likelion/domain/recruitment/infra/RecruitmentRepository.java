package com.snut_likelion.domain.recruitment.infra;

import com.snut_likelion.domain.recruitment.entity.Recruitment;
import com.snut_likelion.domain.recruitment.entity.RecruitmentType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RecruitmentRepository extends JpaRepository<Recruitment, Long> {
    @Query("SELECT r FROM Recruitment r WHERE r.recruitmentType = :recruitmentType " +
            "ORDER BY r.openDate DESC LIMIT 1")
    Optional<Recruitment> findRecentRecruitment(RecruitmentType recruitmentType);

    @EntityGraph(attributePaths = {"questions"})
    Optional<Recruitment> findWithQuestionsById(Long recId);

    // openDate <= now 이면서 아직 startNotified = false 인 모집을 모두 조회
    List<Recruitment> findAllByOpenDateLessThanEqualAndStartNotifiedFalse(LocalDateTime now);
}
