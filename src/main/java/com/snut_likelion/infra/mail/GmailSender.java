package com.snut_likelion.infra.mail;


import com.snut_likelion.global.provider.MailSender;
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

    @Value("${snut.likelion.current-generation}")
    private int currentGeneration;

    @Value("${spring.mail.username}")
    private String sender;

    @Value("${client.url}")
    private String clientUrl;

    private final JavaMailSender mailSender;

    @Override
    public void sendVerificationCode(String toEmail, String code) {
        SimpleMailMessage message = this.generationMessage(
                toEmail,
                "[서울과학기술대학교 멋쟁이사자처럼] 이메일 인증 코드",
                "인증 코드: " + code
        );
        mailSender.send(message);
        log.info("인증 코드 메일 발송 성공: {}, {}", toEmail, code);
    }

    @Override
    public void sendChangePasswordLinkMail(String toEmail, String code) {
        String changePasswordUrl = clientUrl + "/auth/change-password?code=" + code;
        SimpleMailMessage message = this.generationMessage(
                toEmail,
                "[서울과학기술대학교 멋쟁이사자처럼] 비밀번호 변경 페이지 주소",
                "비밀번호 변경 페이지 주소: " + changePasswordUrl
        );
        mailSender.send(message);
        log.info("비밀번호 변경 링크 메일 발송 성공: {}, {}", toEmail, code);
    }

    @Override
    public void sendInterviewScheduledMail(String toEmail, String username, String recruitmentType) {
        String subject = String.format("[서울과학기술대학교 멋쟁이사자처럼] %d %s 서류 결과 안내", currentGeneration, recruitmentType);
        String interviewScheduledMsg = String.format(
                "안녕하세요, 서울과학기술대학교 멋쟁이사자처럼 대학입니다.\n\n" +
                        "지원자님께서는 서울과학기술대학교 멋쟁이사자처럼 %d기 %s 서류 전형에 합격하셨습니다.\n\n" +
                        "진심으로 축하드립니다!!\n\n" +
                        "멋쟁이사자처럼 %d기 %s 면접은 오프라인으로 진행됩니다.\n" +
                        "아래 면접 테이블을 확인하시고, 늦지 않게 와주시길 바랍니다.\n\n" +
                        "대기 장소에서 대기하시다가 지원자님 면접 시간에 맞춰 면접 장소로 이동해주시면 됩니다.\n\n" +
                        "다시 한번, 서류 합격을 축하드립니다! 감사합니다 :)",
                currentGeneration, recruitmentType, currentGeneration, recruitmentType
        );

        SimpleMailMessage message = this.generationMessage(toEmail, subject, interviewScheduledMsg);
        mailSender.send(message);
        log.info("인터뷰 메일 발송 성공: {}, {}, {}", toEmail, username, recruitmentType);
    }

    @Override
    public void sendAcceptedMail(String toEmail, String username, String recruitmentType, String part) {
        String subject = String.format("[서울과학기술대학교 멋쟁이사자처럼] %d %s 최종 결과 안내", currentGeneration, recruitmentType);
        String acceptanceMsg = String.format(
                "안녕하세요, 서울과학기술대학교 멋쟁이사자처럼 대학입니다.\n\n" +
                        "지원자님께서는 서울과학기술대학교 멋쟁이사자처럼 %d기 %s %s 파트에 최종 합격하셨습니다. 진심으로 축하드립니다!!\n\n" +
                        "지원자님과 서울과학기술대학교 멋쟁이사자처럼 %d기를 함께하게 되어 진심으로 기쁩니다. 향후 활동은 공식 노션과 카카오톡 단체 채팅방을 통해 운영 및 진행됩니다.\n\n" +
                        "다시 한번, 합격을 축하드립니다! 감사합니다 :)",
                currentGeneration, recruitmentType, part, currentGeneration
        );
        SimpleMailMessage message = this.generationMessage(toEmail, subject, acceptanceMsg);
        mailSender.send(message);
        log.info("합격 메일 발송 성공: {}, {}, {}", toEmail, username, recruitmentType);
    }

    @Override
    public void sendRejectedMail(String toEmail, String username, String recruitmentType, String part) {
        String subject = String.format("[서울과학기술대학교 멋쟁이사자처럼] %d %s 최종 결과 안내", currentGeneration, recruitmentType);
        String rejectionMsg = String.format(
                "안녕하세요, 서울과학기술대학교 멋쟁이사자처럼 대학입니다.\n\n" +
                        "지원자님께서는 서울과학기술대학교 멋쟁이사자처럼 %d기 %s 파트에 아쉽게도 불합격하셨습니다.\n\n" +
                        "바쁘신 와중에도 지원해 주셔서 진심으로 감사드립니다. 비록 이번에는 좋은 소식을 전해드리지 못했지만,\n" +
                        "지원자님의 열정과 노력에 깊은 감사를 표합니다.\n\n" +
                        "앞으로도 멋쟁이사자처럼 활동에 많은 관심 부탁드리며, 더 나은 기회로 다시 만나뵐 수 있기를 기원합니다.\n\n" +
                        "감사합니다.",
                currentGeneration,
                part
        );

        SimpleMailMessage message = this.generationMessage(toEmail, subject, rejectionMsg);
        mailSender.send(message);
        log.info("불합격 메일 발송 성공: {}, {}, {}", toEmail, username, recruitmentType);
    }

    private SimpleMailMessage generationMessage(String toEmail, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(text);
        return message;
    }


}
