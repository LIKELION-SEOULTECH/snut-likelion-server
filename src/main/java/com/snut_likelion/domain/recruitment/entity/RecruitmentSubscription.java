package com.snut_likelion.domain.recruitment.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecruitmentSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionType subscriptionType;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder
    public RecruitmentSubscription(Long id, String email, SubscriptionType subscriptionType) {
        this.id = id;
        this.email = email;
        this.subscriptionType = subscriptionType;
        this.createdAt = LocalDateTime.now();
    }

    public static RecruitmentSubscription of(String email, SubscriptionType subscriptionType) {
        return RecruitmentSubscription.builder()
                .email(email)
                .subscriptionType(subscriptionType)
                .build();
    }
}
