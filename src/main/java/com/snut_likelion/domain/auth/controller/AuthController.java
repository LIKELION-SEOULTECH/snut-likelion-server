package com.snut_likelion.domain.auth.controller;

import com.snut_likelion.domain.auth.dto.ChangePasswordRequest;
import com.snut_likelion.domain.auth.dto.RegisterReq;
import com.snut_likelion.domain.auth.dto.ResetPasswordRequest;
import com.snut_likelion.domain.auth.service.AuthMailService;
import com.snut_likelion.domain.auth.service.AuthService;
import com.snut_likelion.global.auth.dto.TokenDto;
import com.snut_likelion.global.auth.jwt.JwtService;
import com.snut_likelion.global.auth.model.SnutLikeLionUser;
import com.snut_likelion.global.dto.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.snut_likelion.global.auth.jwt.JwtProvider.REFRESH_TOKEN_HEADER;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final AuthMailService authMailService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Object> register(@RequestBody @Valid RegisterReq req) {
        authService.register(req);
        return ApiResponse.success("회원가입 성공");
    }

    @GetMapping("/refresh")
    public TokenDto refresh(
            HttpServletResponse response,
            @RequestHeader(value = REFRESH_TOKEN_HEADER) String bearerRefreshToken
    ) {
        TokenDto tokenDto = jwtService.tokenRefresh(bearerRefreshToken);
        jwtService.setCookie(tokenDto, response);
        return tokenDto;
    }

    @PostMapping("/email/send")
    public ApiResponse<Object> sendCertifyEmail(
            @RequestParam("email") String email
    ) {
        authMailService.sendCertifyEmail(email);
        return ApiResponse.success("인증 코드 전송 완료");
    }

    @PostMapping("/email/certify")
    public ApiResponse<Object> certifyCode(
            @RequestParam("email") String email,
            @RequestParam("code") String code
    ) {
        authService.certifyCode(email, code);
        return ApiResponse.success("이메일 인증 성공");
    }

    @PostMapping("/password/find")
    public ApiResponse<Object> sendPasswordEmail(
            @RequestParam("email") String email
    ) {
        authMailService.sendFindPasswordEmail(email);
        return ApiResponse.success("비밀번호 찾기 메일 전송 완료");
    }

    @PatchMapping("/password/reset")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resetPassword(
            @RequestBody @Valid ResetPasswordRequest req
    ) {
        authService.resetPassword(req);
    }

    @PatchMapping("/password/change")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(
            @AuthenticationPrincipal SnutLikeLionUser loginUser,
            @RequestBody @Valid ChangePasswordRequest req
    ) {
        authService.changePassword(loginUser.getId(), req);
    }
}
