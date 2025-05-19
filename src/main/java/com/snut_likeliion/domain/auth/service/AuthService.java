package com.snut_likeliion.domain.auth.service;

import com.snut_likeliion.domain.auth.dto.RegisterReq;
import com.snut_likeliion.domain.auth.entity.CertificationToken;
import com.snut_likeliion.domain.auth.exception.AuthErrorCode;
import com.snut_likeliion.domain.auth.provider.MailSender;
import com.snut_likeliion.domain.auth.repository.CertificationTokenRepository;
import com.snut_likeliion.domain.user.entity.Role;
import com.snut_likeliion.domain.user.exception.UserErrorCode;
import com.snut_likeliion.domain.user.repository.UserRepository;
import com.snut_likeliion.global.error.exception.BadRequestException;
import com.snut_likeliion.global.error.exception.ExistingResourceException;
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

    @Value("${admin.email}")
    private String ADMIN_EMAIL;

    private final UserRepository userRepository;
    private final CertificationTokenRepository certificationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailSender mailSender;

    @Transactional
    public void register(RegisterReq req) {
        Role role = this.isAdminEmail(req.getEmail())
                ? Role.ROLE_ADMIN
                : Role.ROLE_USER;

        boolean isExists = userRepository.existsByEmailOrUsername(req.getEmail(), req.getUsername());
        if (isExists) throw new ExistingResourceException(UserErrorCode.EXISTING_USER);

        userRepository.save(req.toEntity(role, passwordEncoder));
    }

    private boolean isAdminEmail(String email) {
        return email.equals(ADMIN_EMAIL);
    }

    @Transactional
    public void sendCertifyEmail(String email) {
        String code = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        CertificationToken certificationToken = CertificationToken.builder()
                .email(email)
                .code(code)
                .expiredAt(LocalDateTime.now().plusMinutes(10))
                .build();

        certificationTokenRepository.save(certificationToken);

        mailSender.sendVerificationCode(email, code);
    }

    @Transactional
    public void certifyCode(String email, String code) {
        CertificationToken certificationToken = certificationTokenRepository.findByEmail(email)
                .filter(t -> t.getExpiredAt().isAfter(LocalDateTime.now()))
                .filter(t -> t.getCode().equals(code))
                .orElseThrow(() -> new BadRequestException(AuthErrorCode.INVALID_CERTIFICATION_TOKEN));

        certificationTokenRepository.delete(certificationToken);
    }
}
