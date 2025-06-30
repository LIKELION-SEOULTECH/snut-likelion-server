package com.snut_likelion.domain.auth.service;

import com.snut_likelion.domain.auth.dto.ChangePasswordRequest;
import com.snut_likelion.domain.auth.dto.RegisterReq;
import com.snut_likelion.domain.auth.dto.ResetPasswordRequest;
import com.snut_likelion.domain.auth.entity.CertificationToken;
import com.snut_likelion.domain.auth.exception.AuthErrorCode;
import com.snut_likelion.domain.auth.repository.CertificationTokenRepository;
import com.snut_likelion.domain.user.entity.User;
import com.snut_likelion.domain.user.exception.UserErrorCode;
import com.snut_likelion.domain.user.repository.UserRepository;
import com.snut_likelion.global.error.exception.BadRequestException;
import com.snut_likelion.global.error.exception.ExistingResourceException;
import com.snut_likelion.global.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final CertificationTokenRepository certificationTokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void register(RegisterReq req) {
        boolean isExists = userRepository.existsByEmailOrUsernameOrPhoneNumber(req.getEmail(), req.getUsername(), req.getPhoneNumber());
        if (isExists) throw new ExistingResourceException(UserErrorCode.EXISTING_USER);
        User user = req.toEntity(passwordEncoder);
        userRepository.save(user);
    }

    @Transactional
    public void certifyCode(String email, String code) {
        CertificationToken certificationToken = this.getCertificationTokenByEmailAndCode(email, code);
        certificationTokenRepository.delete(certificationToken);
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest req) {
        CertificationToken certificationToken = this.getCertificationTokenByEmailAndCode(req.getEmail(), req.getCode());
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

    @Transactional
    public void changePassword(Long userId, ChangePasswordRequest req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(UserErrorCode.NOT_FOUND));

        user.updatePassword(passwordEncoder.encode(req.getNewPassword()));
    }
}
