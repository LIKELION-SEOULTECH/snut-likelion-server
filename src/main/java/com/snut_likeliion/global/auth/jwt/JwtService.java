package com.snut_likeliion.global.auth.jwt;

import com.snut_likeliion.domain.auth.entity.RefreshToken;
import com.snut_likeliion.domain.auth.exception.AuthErrorCode;
import com.snut_likeliion.domain.auth.repository.RefreshTokenRepository;
import com.snut_likeliion.domain.user.entity.User;
import com.snut_likeliion.domain.user.exception.UserErrorCode;
import com.snut_likeliion.domain.user.repository.UserRepository;
import com.snut_likeliion.global.auth.dto.TokenDto;
import com.snut_likeliion.global.auth.model.SnutLikeLionUser;
import com.snut_likeliion.global.auth.model.UserInfo;
import com.snut_likeliion.global.error.GlobalErrorCode;
import com.snut_likeliion.global.error.exception.NotFoundException;
import com.snut_likeliion.global.error.exception.UnauthorizedException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import static com.snut_likeliion.global.auth.jwt.JwtProvider.BEARER_PREFIX;


@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Value("${snut.likelion.current-generation}")
    private int currentGeneration;

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public SnutLikeLionUser getPrincipal(String accessToken) {
        return SnutLikeLionUser.from(jwtProvider.extractMemberDTOFromAccessToken(accessToken));
    }

    @Transactional
    public void logout(String bearerRefreshToken) {
        String email = this.extractUsernameFromRefreshToken(bearerRefreshToken);
        log.debug("로그아웃 === 유저명: {}", email);
        refreshTokenRepository.deleteAllByEmail(email);
    }

    private String extractUsernameFromRefreshToken(String bearerRefreshToken) {
        return jwtProvider.getEmail(this.getTokenFromBearer(bearerRefreshToken));
    }

    public void validate(String token) {
        try {
            jwtProvider.getPayload(token);
        } catch (SecurityException e) {
            throw new UnauthorizedException(GlobalErrorCode.INVALID_TOKEN, "검증 정보가 올바르지 않습니다.");
        } catch (MalformedJwtException e) {
            throw new UnauthorizedException(GlobalErrorCode.INVALID_TOKEN, "유효하지 않은 토큰입니다.");
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException(GlobalErrorCode.INVALID_TOKEN, "기한이 만료된 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            throw new UnauthorizedException(GlobalErrorCode.INVALID_TOKEN, "지원되지 않는 토큰입니다.");
        }
    }

    @Transactional
    public TokenDto tokenRefresh(String bearerRefreshToken) {
        String refreshTokenPayload = this.getTokenFromBearer(bearerRefreshToken);
        this.validate(refreshTokenPayload);
        SnutLikeLionUser SnutLikeLionUser = this.extractSnutLikeLionUserFromRefreshToken(refreshTokenPayload);
        log.debug("토큰 갱신 === 유저: {}", SnutLikeLionUser);
        return this.doTokenGenerationProcess(SnutLikeLionUser);
    }

    private SnutLikeLionUser extractSnutLikeLionUserFromRefreshToken(String refreshTokenPayload) {
        RefreshToken findRefreshToken = refreshTokenRepository.findByPayload(refreshTokenPayload)
                .orElseThrow(() -> new NotFoundException(AuthErrorCode.NOT_FOUND_REFRESH_TOKEN));
        User user = userRepository.findByEmail(findRefreshToken.getEmail())
                .orElseThrow(() -> new NotFoundException(UserErrorCode.NOT_FOUND));
        return SnutLikeLionUser.from(UserInfo.from(user, currentGeneration));
    }

    @Transactional
    public TokenDto doTokenGenerationProcess(SnutLikeLionUser principal) {
        TokenDto tokenDto = jwtProvider.createJwt(principal);
        refreshTokenRepository.findByEmail(principal.getEmail())
                .ifPresentOrElse(
                        // 있다면 새토큰 발급후 업데이트
                        token -> token.updatePayload(tokenDto.getRefreshToken()),
                        // 없다면 새로 만들고 DB에 저장
                        () -> refreshTokenRepository.save(RefreshToken.of(tokenDto.getRefreshToken(), principal.getEmail()))
                );
        return tokenDto;
    }

    public void setCookie(TokenDto tokenDto, HttpServletResponse response) {
        boolean isProd = "prod".equals(activeProfile);
        String sameSite = isProd ? "None" : null; // 개발 환경에서는 sameSite를 null로 설정
        String domain = ".snut-likelion.com";

        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", tokenDto.getAccessToken())
                .path("/")
                .httpOnly(true)
                .secure(isProd)
                .sameSite(sameSite)
                .domain(domain)
                .maxAge((int) (jwtProvider.getAccessTokenExpiration() / 1000))
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", tokenDto.getRefreshToken())
                .path("/")
                .httpOnly(true)
                .secure(isProd)
                .sameSite(sameSite)
                .domain(domain)
                .maxAge((int) (jwtProvider.getRefreshTokenExpiration() / 1000))
                .build();

        response.addHeader("Set-Cookie", accessTokenCookie.toString());
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());
    }

    /**
     * Bearer Prefix를 포함한 값을 전달받으면 토큰만을 추출하여 반환
     *
     * @param bearerToken
     * @return Token (String)
     */
    public String getTokenFromBearer(String bearerToken) {
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.split(" ")[1];
        }
        return null;
    }
}
