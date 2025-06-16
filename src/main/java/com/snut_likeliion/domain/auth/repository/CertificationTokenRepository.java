package com.snut_likeliion.domain.auth.repository;

import com.snut_likeliion.domain.auth.entity.CertificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CertificationTokenRepository extends JpaRepository<CertificationToken, Long> {
    Optional<CertificationToken> findByEmail(String email);
}
