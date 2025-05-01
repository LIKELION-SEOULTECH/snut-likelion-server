package com.snut_likeliion.domain.auth.service;

import com.snut_likeliion.domain.auth.dto.RegisterReq;
import com.snut_likeliion.domain.user.entity.Role;
import com.snut_likeliion.domain.user.exception.UserErrorCode;
import com.snut_likeliion.domain.user.repository.UserRepository;
import com.snut_likeliion.global.error.exception.ExistingResourceException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${admin.email}")
    private String ADMIN_EMAIL;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
}
