package com.snut_likelion.global.provider;

import com.snut_likelion.domain.recruitment.entity.RecruitmentType;

import java.time.LocalDateTime;

public interface MailSender {

    void sendVerificationCode(String toEmail, String code);

    void sendChangePasswordLinkMail(String toEmail, String code);

    void sendInterviewScheduledMail(String toEmail, String username, String recruitmentType);

    void sendAcceptedMail(String toEmail, String username, String recruitmentType, String part);

    void sendRejectedMail(String toEmail, String username, String recruitmentType, String part);

<<<<<<< HEAD
    void sendRecruitmentStartNotification(String toEmail, String username, int generation, String recruitmentType, LocalDateTime openDate, LocalDateTime closeDate);
=======
    void sendRecruitmentStartNotification(String toEmail, int generation, String recruitmentType, LocalDateTime openDate, LocalDateTime closeDate);
>>>>>>> 6de69ba85508f0cbec27e57958f0783643f34360
}
