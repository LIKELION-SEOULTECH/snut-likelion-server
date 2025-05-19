package com.snut_likeliion.infra.mail;


import com.snut_likeliion.domain.auth.provider.MailSender;
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

    private final JavaMailSender mailSender;

    @Override
    public void sendVerificationCode(String toEmail, String code) {
        log.info("이메일 발송!");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(toEmail);
        message.setSubject("[SNUT_LIKELION] 이메일 인증 코드");
        message.setText("인증 코드: " + code);
        mailSender.send(message);
    }
}
