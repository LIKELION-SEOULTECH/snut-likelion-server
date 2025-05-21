package com.snut_likeliion.domain.auth.service;

import com.snut_likeliion.domain.auth.dto.ChangePasswordRequest;
import com.snut_likeliion.domain.auth.dto.FindPasswordRequest;
import com.snut_likeliion.domain.auth.dto.RegisterReq;
import com.snut_likeliion.domain.auth.entity.CertificationToken;
import com.snut_likeliion.domain.auth.exception.AuthErrorCode;
import com.snut_likeliion.domain.auth.provider.MailSender;
import com.snut_likeliion.domain.auth.repository.CertificationTokenRepository;
import com.snut_likeliion.domain.auth.repository.GenerationRepository;
import com.snut_likeliion.domain.user.entity.Generation;
import com.snut_likeliion.domain.user.entity.User;
import com.snut_likeliion.domain.user.exception.UserErrorCode;
import com.snut_likeliion.domain.user.repository.UserRepository;
import com.snut_likeliion.global.error.exception.BadRequestException;
import com.snut_likeliion.global.error.exception.ExistingResourceException;
import com.snut_likeliion.global.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final CertificationTokenRepository certificationTokenRepository;
    private final GenerationRepository generationRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailSender mailSender;

    @Transactional
    public void register(RegisterReq req) {
        boolean isExists = userRepository.existsByEmailOrUsername(req.getEmail(), req.getUsername());
        if (isExists) throw new ExistingResourceException(UserErrorCode.EXISTING_USER);
        User user = userRepository.save(req.toEntity(passwordEncoder));
        generationRepository.save(Generation.of(user.getId(), user.getGeneration()));
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
