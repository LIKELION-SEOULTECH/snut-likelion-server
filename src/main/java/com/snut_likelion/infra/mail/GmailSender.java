package com.snut_likelion.infra.mail;


import com.snut_likelion.domain.auth.provider.MailSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GmailSender implements MailSender {

    @Value("${spring.mail.username}")
    private String sender;

    @Value("${client.url}")
    private String clientUrl;

    private final JavaMailSender mailSender;

    @Override
    public void sendVerificationCode(String toEmail, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(toEmail);
        message.setSubject("[SNUT_LIKELION] 이메일 인증 코드");
        message.setText("인증 코드: " + code);
        mailSender.send(message);
    }

    @Override
    public void sendChangePasswordLinkMail(String toEmail, String code) {
        String changePasswordUrl = clientUrl + "/auth/change-password?code=" + code;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(toEmail);
        message.setSubject("[SNUT_LIKELION] 비밀번호 변경 페이지 주소");
        message.setText("비밀번호 변경 페이지 주소: " + changePasswordUrl);
        mailSender.send(message);
    }
}
