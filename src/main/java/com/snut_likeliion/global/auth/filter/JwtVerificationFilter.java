package com.snut_likeliion.global.auth.filter;

import com.snut_likeliion.global.auth.jwt.JwtService;
import com.snut_likeliion.global.auth.model.RestAuthenticationToken;
import com.snut_likeliion.global.auth.model.SnutLikeLionUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.snut_likeliion.global.auth.jwt.JwtProvider.ACCESS_TOKEN_HEADER;

@Component
@RequiredArgsConstructor
public class JwtVerificationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String bearerToken = request.getHeader(ACCESS_TOKEN_HEADER);
        String accessToken = jwtService.getTokenFromBearer(bearerToken);

        if (!StringUtils.hasText(accessToken)) {
            filterChain.doFilter(request, response);
            return;
        }

        jwtService.validate(accessToken);
        setAuthenticationToContext(accessToken);
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return super.shouldNotFilter(request);
    }

    private void setAuthenticationToContext(String token) {
        SnutLikeLionUser snutLikeLionUser = jwtService.getPrincipal(token);
        RestAuthenticationToken authentication = RestAuthenticationToken.authenticated(snutLikeLionUser);
        SecurityContext securityContext = SecurityContextHolder.getContextHolderStrategy().createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.getContextHolderStrategy().setContext(securityContext);
    }
}
