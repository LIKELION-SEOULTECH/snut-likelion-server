package com.snut_likeliion.domain.auth.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "certification_tokens")
public class CertificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String code;

    private LocalDateTime expiredAt;

    @Builder
    public CertificationToken(Long id, String email, String code, LocalDateTime expiredAt) {
        this.id = id;
        this.email = email;
        this.code = code;
        this.expiredAt = expiredAt;
    }
}
