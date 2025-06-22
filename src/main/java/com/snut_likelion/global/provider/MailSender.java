package com.snut_likelion.global.provider;

public interface MailSender {

    void sendVerificationCode(String toEmail, String code);

    void sendChangePasswordLinkMail(String toEmail, String code);

    void sendInterviewScheduledMail(String toEmail, String username, String recruitmentType);

    void sendAcceptedMail(String toEmail, String username, String recruitmentType, String part);

    void sendRejectedMail(String toEmail, String username, String recruitmentType, String part);
}
