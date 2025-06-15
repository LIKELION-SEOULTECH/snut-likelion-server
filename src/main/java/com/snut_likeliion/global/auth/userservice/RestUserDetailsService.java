package com.snut_likeliion.global.auth.userservice;

import com.snut_likeliion.domain.user.entity.User;
import com.snut_likeliion.domain.user.exception.UserErrorCode;
import com.snut_likeliion.domain.user.repository.UserRepository;
import com.snut_likeliion.global.auth.model.SnutLikeLionUser;
import com.snut_likeliion.global.auth.model.UserInfo;
import com.snut_likeliion.global.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RestUserDetailsService implements UserDetailsService {

    @Value("${snut.likelion.current-generation}")
    private int currentGeneration;

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public SnutLikeLionUser loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findWithLionInfoByEmail(email)
                .orElseThrow(() -> new NotFoundException(UserErrorCode.NOT_FOUND));
        return SnutLikeLionUser.from(UserInfo.from(user, currentGeneration));
    }
}
