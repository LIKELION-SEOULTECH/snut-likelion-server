package com.snut_likeliion.domain.user.repository;

import com.snut_likeliion.domain.user.entity.LionInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LionInfoRepository extends JpaRepository<LionInfo, Long> {

    Optional<LionInfo> findByUser_IdAndGeneration(Long userId, int generation);

    List<LionInfo> findAllByGeneration(int generation);

    @Query("select li.generation from LionInfo li " +
            "where li.user.id = :memberId")
    List<Integer> findGenerationsByUser_Id(Long memberId);
}
