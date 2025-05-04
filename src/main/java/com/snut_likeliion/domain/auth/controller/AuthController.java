package com.snut_likeliion.domain.auth.controller;

import com.snut_likeliion.domain.auth.dto.RegisterReq;
import com.snut_likeliion.domain.auth.service.AuthService;
import com.snut_likeliion.global.auth.dto.TokenDto;
import com.snut_likeliion.global.auth.jwt.JwtService;
import com.snut_likeliion.global.dto.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.snut_likeliion.global.auth.jwt.JwtProvider.REFRESH_TOKEN_HEADER;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Void> register(@RequestBody @Valid RegisterReq req) {
        authService.register(req);
        return ApiResponse.success("회원가입 성공");
    }

    @GetMapping("/refresh")
    public ApiResponse<Void> refresh(
            HttpServletResponse response,
            @RequestHeader(value = REFRESH_TOKEN_HEADER) String bearerRefreshToken
    ) {
        TokenDto tokenDto = jwtService.tokenRefresh(bearerRefreshToken);
        jwtService.setCookie(tokenDto, response);
        return ApiResponse.success("Token Refresh 성공");
    }
}
