package com.snut_likelion.domain.recruitment.schedule;

import com.snut_likelion.domain.recruitment.entity.Recruitment;
<<<<<<< HEAD
import com.snut_likelion.domain.recruitment.infra.RecruitmentRepository;
import com.snut_likelion.admin.recruitment.service.NotificationService;
=======
import com.snut_likelion.domain.recruitment.entity.RecruitmentSubscription;
import com.snut_likelion.domain.recruitment.entity.RecruitmentType;
import com.snut_likelion.domain.recruitment.entity.SubscriptionType;
import com.snut_likelion.domain.recruitment.infra.RecruitmentRepository;
import com.snut_likelion.domain.recruitment.infra.RecruitmentSubscriptionRepository;
import com.snut_likelion.domain.recruitment.service.NotificationService;
>>>>>>> 6de69ba85508f0cbec27e57958f0783643f34360
import com.snut_likelion.domain.user.entity.User;
import com.snut_likelion.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
<<<<<<< HEAD
import java.util.List;
=======
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
>>>>>>> 6de69ba85508f0cbec27e57958f0783643f34360

@Component
@RequiredArgsConstructor
public class RecruitmentNotifyScheduler {

    private final RecruitmentRepository recruitmentRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
<<<<<<< HEAD

    @Scheduled(cron = "0 0 19 * * *") // 매일 00:00
=======
    private final RecruitmentSubscriptionRepository recruitmentSubscriptionRepository;

    @Scheduled(cron = "0 * * * * *") // 매일 19:00
>>>>>>> 6de69ba85508f0cbec27e57958f0783643f34360
    @Transactional
    public void sendRecruitmentStartNotifications() {
        LocalDateTime now = LocalDateTime.now();

        // 아직 알림 안 보냈고, openDate <= now인 모집들 조회
        List<Recruitment> toNotify = recruitmentRepository
                .findAllByOpenDateLessThanEqualAndStartNotifiedFalse(now);
<<<<<<< HEAD

        if (toNotify.isEmpty()) return;

        // 모든 유저 조회
        List<User> targets = userRepository.findAll();

        for (Recruitment rec : toNotify) {
            for (User user : targets) {
                // 이메일 전송
                notificationService.sendRecruitmentStartNotice(user, rec);
            }

=======
        if (toNotify.isEmpty()) return;

        // 모든 유저 이메일
        Set<String> userEmails = userRepository.findAll().stream()
                .map(User::getEmail)
                .collect(Collectors.toSet());

        for (Recruitment rec : toNotify) {
            // 1) 타입별 구독자 조회
            SubscriptionType subType = rec.getRecruitmentType() == RecruitmentType.MEMBER
                    ? SubscriptionType.MEMBER
                    : SubscriptionType.MANAGER;

            List<RecruitmentSubscription> subs = recruitmentSubscriptionRepository
                    .findAllBySubscriptionType(subType);

            // 2) 구독자 이메일 Set
            Set<String> subscriberEmails = subs.stream()
                    .map(RecruitmentSubscription::getEmail)
                    .collect(Collectors.toSet());

            // 3) union → 중복 제거
            Set<String> allEmails = new HashSet<>(userEmails);
            allEmails.addAll(subscriberEmails);

            // 4) 알림 발송 (한 번만)
            for (String email : allEmails) {
                notificationService.sendRecruitmentStartNotice(email, rec);
            }

            // 5) 처리된 구독자 삭제
            recruitmentSubscriptionRepository.deleteAll(subs);

            // 6) 이 모집은 다시 알림 안 보내도록 표시
>>>>>>> 6de69ba85508f0cbec27e57958f0783643f34360
            rec.markStartNotified();
        }

        recruitmentRepository.saveAll(toNotify);
    }
}
