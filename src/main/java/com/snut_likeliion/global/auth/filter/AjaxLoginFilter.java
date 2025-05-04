package com.snut_likeliion.global.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snut_likeliion.domain.user.exception.UserErrorCode;
import com.snut_likeliion.global.auth.dto.LoginReq;
import com.snut_likeliion.global.auth.dto.TokenDto;
import com.snut_likeliion.global.auth.jwt.JwtService;
import com.snut_likeliion.global.auth.model.AjaxAuthenticationToken;
import com.snut_likeliion.global.auth.model.SnutLikeLionUser;
import com.snut_likeliion.global.dto.ApiResponse;
import com.snut_likeliion.global.error.GlobalErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

public class AjaxLoginFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper;
    private final JwtService jwtService;

    public AjaxLoginFilter(ObjectMapper objectMapper, JwtService jwtService) {
        super(new AntPathRequestMatcher("/api/v1/auth/login", "POST"));
        this.objectMapper = objectMapper;
        this.jwtService = jwtService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        LoginReq loginReq = objectMapper.readValue(request.getReader(), LoginReq.class);
        AjaxAuthenticationToken unauthenticated = AjaxAuthenticationToken.unauthenticated(
                loginReq.getEmail(),
                loginReq.getPassword()
        );
        return this.getAuthenticationManager().authenticate(unauthenticated);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        SnutLikeLionUser principal = (SnutLikeLionUser) authResult.getPrincipal();
        TokenDto tokenDto = jwtService.doTokenGenerationProcess(principal);
        jwtService.setCookie(tokenDto, response);
        objectMapper.writeValue(response.getWriter(), tokenDto);

//        ApiResponse responseDto = ApiResponse.success(principal.getUsername(), "로그인 성공");
//        objectMapper.writeValue(response.getWriter(), responseDto);

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ApiResponse responseDto = null;

        if (failed instanceof BadCredentialsException) {
            responseDto = ApiResponse.fail(GlobalErrorCode.INVALID_AUTH_DATA);
        } else if (failed instanceof UsernameNotFoundException) {
            responseDto = ApiResponse.fail(UserErrorCode.NOT_FOUND);
        }

        objectMapper.writeValue(response.getWriter(), responseDto);
    }


}
