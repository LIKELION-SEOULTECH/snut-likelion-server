package com.snut_likelion.domain.recruitment.infra;

import com.snut_likelion.domain.recruitment.entity.RecruitmentSubscription;
import com.snut_likelion.domain.recruitment.entity.SubscriptionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecruitmentSubscriptionRepository extends JpaRepository<RecruitmentSubscription, Long> {

    List<RecruitmentSubscription> findAllBySubscriptionType(SubscriptionType type);

    boolean existsByEmailAndSubscriptionType(String email, SubscriptionType type);
}
