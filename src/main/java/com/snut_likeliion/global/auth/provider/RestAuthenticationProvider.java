package com.snut_likeliion.global.auth.provider;

import com.snut_likeliion.global.auth.model.RestAuthenticationToken;
import com.snut_likeliion.global.auth.model.SnutLikeLionUser;
import com.snut_likeliion.global.auth.userservice.RestUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RestAuthenticationProvider implements AuthenticationProvider {

    private final RestUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = (String) authentication.getCredentials();

        SnutLikeLionUser snutLikeLionUser = userDetailsService.loadUserByUsername(email);

        if (!passwordEncoder.matches(password, snutLikeLionUser.getPassword())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        return RestAuthenticationToken.authenticated(snutLikeLionUser);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(RestAuthenticationToken.class);
    }
}
