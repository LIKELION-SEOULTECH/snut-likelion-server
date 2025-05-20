package com.snut_likeliion.domain.auth.repository;

import com.snut_likeliion.domain.user.entity.Generation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenerationRepository extends JpaRepository<Generation, Long> {
}
