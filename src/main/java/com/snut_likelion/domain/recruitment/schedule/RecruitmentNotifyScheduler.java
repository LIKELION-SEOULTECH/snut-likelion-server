package com.snut_likelion.domain.recruitment.schedule;

import com.snut_likelion.domain.recruitment.entity.Recruitment;
import com.snut_likelion.domain.recruitment.infra.RecruitmentRepository;
import com.snut_likelion.domain.recruitment.service.NotificationService;
import com.snut_likelion.domain.user.entity.User;
import com.snut_likelion.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RecruitmentNotifyScheduler {

    private final RecruitmentRepository recruitmentRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Scheduled(cron = "0 0 19 * * *") // 매일 00:00
    @Transactional
    public void sendRecruitmentStartNotifications() {
        LocalDateTime now = LocalDateTime.now();

        // 아직 알림 안 보냈고, openDate <= now인 모집들 조회
        List<Recruitment> toNotify = recruitmentRepository
                .findAllByOpenDateLessThanEqualAndStartNotifiedFalse(now);

        if (toNotify.isEmpty()) return;

        // 모든 유저 조회
        List<User> targets = userRepository.findAll();

        for (Recruitment rec : toNotify) {
            for (User user : targets) {
                // 이메일 전송
                notificationService.sendRecruitmentStartNotice(user, rec);
            }

            rec.markStartNotified();
        }

        recruitmentRepository.saveAll(toNotify);
    }
}
