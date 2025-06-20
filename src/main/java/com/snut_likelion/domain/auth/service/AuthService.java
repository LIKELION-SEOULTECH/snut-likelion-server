package com.snut_likelion.domain.auth.service;

import com.snut_likelion.domain.auth.dto.ChangePasswordRequest;
import com.snut_likelion.domain.auth.dto.FindPasswordRequest;
import com.snut_likelion.domain.auth.dto.RegisterReq;
import com.snut_likelion.domain.auth.entity.CertificationToken;
import com.snut_likelion.domain.auth.exception.AuthErrorCode;
import com.snut_likelion.domain.auth.provider.MailSender;
import com.snut_likelion.domain.auth.repository.CertificationTokenRepository;
import com.snut_likelion.domain.user.entity.Part;
import com.snut_likelion.domain.user.entity.User;
import com.snut_likelion.domain.user.exception.UserErrorCode;
import com.snut_likelion.domain.user.repository.UserRepository;
import com.snut_likelion.global.error.exception.BadRequestException;
import com.snut_likelion.global.error.exception.ExistingResourceException;
import com.snut_likelion.global.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${snut.likelion.current-generation}")
    private int currentGeneration;

    private final UserRepository userRepository;
    private final CertificationTokenRepository certificationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailSender mailSender;

    @Transactional
    public void register(RegisterReq req) {
        boolean isExists = userRepository.existsByEmailOrUsername(req.getEmail(), req.getUsername());
        if (isExists) throw new ExistingResourceException(UserErrorCode.EXISTING_USER);
        User user = req.toEntity(passwordEncoder);
        // 임시) 회원가입 시 현재 기수로 동아리 활동 정보 생성
        user.generateCurrentLionInfo(currentGeneration, Part.BACKEND);
        userRepository.save(user);
    }

    @Transactional
    public void sendCertifyEmail(String email) {
        String code = this.generateCertificationCode(email);
        mailSender.sendVerificationCode(email, code);
    }

    @Transactional
    public void certifyCode(String email, String code) {
        CertificationToken certificationToken = this.getCertificationTokenByEmailAndCode(email, code);
        certificationTokenRepository.delete(certificationToken);
    }

    @Transactional
    public void sendFindPasswordEmail(FindPasswordRequest req) {
        boolean isExists = userRepository.existsByEmailAndUsername(req.getEmail(), req.getName());
        if (!isExists) throw new NotFoundException(UserErrorCode.NOT_FOUND);
        String code = this.generateCertificationCode(req.getEmail());
        mailSender.sendChangePasswordLinkMail(req.getEmail(), code);
    }

    private String generateCertificationCode(String email) {
        String code = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        CertificationToken certificationToken = CertificationToken.builder()
                .email(email)
                .code(code)
                .expiredAt(LocalDateTime.now().plusMinutes(10))
                .build();

        certificationTokenRepository.save(certificationToken);
        return code;
    }

    @Transactional
    public void changePassword(ChangePasswordRequest req) {
        CertificationToken certificationToken = getCertificationTokenByEmailAndCode(req.getEmail(), req.getCode());
        certificationTokenRepository.delete(certificationToken);

        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new NotFoundException(UserErrorCode.NOT_FOUND));

        user.updatePassword(passwordEncoder.encode(req.getNewPassword()));
    }

    private CertificationToken getCertificationTokenByEmailAndCode(String email, String code) {
        return certificationTokenRepository.findByEmail(email)
                .filter(t -> t.getExpiredAt().isAfter(LocalDateTime.now()))
                .filter(t -> t.getCode().equals(code))
                .orElseThrow(() -> new BadRequestException(AuthErrorCode.INVALID_CERTIFICATION_TOKEN));
    }
}
