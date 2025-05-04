package com.snut_likeliion.global.auth.handlers;

import com.snut_likeliion.global.auth.jwt.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.snut_likeliion.global.auth.jwt.JwtProvider.REFRESH_TOKEN_HEADER;

@Component
@RequiredArgsConstructor
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    private final JwtService jwtService;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String bearerRefreshToken = request.getHeader(REFRESH_TOKEN_HEADER);
        jwtService.logout(bearerRefreshToken);
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}