package com.snut_likeliion.domain.auth.provider;

public interface MailSender {

    void sendVerificationCode(String toEmail, String code);

    void sendChangePasswordLinkMail(String toEmail, String code);
}
