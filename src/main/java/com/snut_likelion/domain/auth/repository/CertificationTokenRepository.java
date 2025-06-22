package com.snut_likelion.domain.auth.repository;

import com.snut_likelion.domain.auth.entity.CertificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CertificationTokenRepository extends JpaRepository<CertificationToken, Long> {

    @Query("SELECT ct FROM CertificationToken ct WHERE ct.email = :email ORDER BY ct.expiredAt DESC LIMIT 1")
    Optional<CertificationToken> findByEmail(String email);
}
