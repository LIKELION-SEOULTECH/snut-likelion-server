package com.snut_likelion.global.auth.jwt;

import com.snut_likelion.global.auth.dto.TokenDto;
import com.snut_likelion.global.auth.model.SnutLikeLionUser;
import com.snut_likelion.global.auth.model.UserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;

@Component
public class JwtProvider {

    public static final String BEARER_PREFIX = "Bearer ";
    public static final String ACCESS_TOKEN_HEADER = "Authorization";
    public static final String REFRESH_TOKEN_HEADER = "Refresh";

    @Value("${jwt.access-token.expiration}")
    private Long accessTokenExpiration;

    @Value("${jwt.refresh-token.expiration}")
    private Long refreshTokenExpiration;

    private SecretKey secretKey;
    private JwtParser jwtParser;

    public JwtProvider(@Value("${jwt.secret-key}") String secretKey) {
        this.secretKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        this.jwtParser = Jwts.parser()
                .verifyWith(this.secretKey)
                .build();
    }

    public TokenDto createJwt(SnutLikeLionUser snutLikeLionUser) {
        String accessToken = this.generateAccessToken(snutLikeLionUser, new Date());
        String refreshToken = this.generateRefreshToken(snutLikeLionUser.getEmail(), new Date());

        return TokenDto.builder()
                .email(snutLikeLionUser.getEmail())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public String generateAccessToken(SnutLikeLionUser SnutLikeLionUser, Date now) {
        ArrayList<? extends GrantedAuthority> authorities =
                (ArrayList<? extends GrantedAuthority>) SnutLikeLionUser.getAuthorities();

        return Jwts.builder()
                .subject(SnutLikeLionUser.getEmail())
                .claim("id", SnutLikeLionUser.getId())
                .claim("username", SnutLikeLionUser.getUsername())
                .claim("role", authorities.get(0).getAuthority())
                .issuedAt(now)
                .expiration(new Date(now.getTime() + accessTokenExpiration))
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken(String email, Date now) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + refreshTokenExpiration))
                .signWith(secretKey)
                .compact();
    }

    public UserInfo extractMemberDTOFromAccessToken(String accessToken) {
        return UserInfo.builder()
                .id(this.getId(accessToken))
                .username(this.getUsername(accessToken))
                .role(this.getRole(accessToken))
                .build();
    }

    public Long getAccessTokenExpiration() {
        return accessTokenExpiration;
    }

    public Long getRefreshTokenExpiration() {
        return refreshTokenExpiration;
    }

    /**
     * 서명된 토큰 값을 파싱하여 payload를 추출
     *
     * @param token
     * @return claims(payload)
     */
    public Claims getPayload(String token) {
        return jwtParser
                .parseSignedClaims(token)
                .getPayload();
    }

    public Long getId(String token) {
        return getPayload(token).get("id", Long.class);
    }

    public String getEmail(String token) {
        return getPayload(token).getSubject();
    }

    public String getUsername(String token) {
        return getPayload(token).get("username", String.class);
    }

    public String getRole(String token) {
        return getPayload(token).get("role", String.class);
    }
}
