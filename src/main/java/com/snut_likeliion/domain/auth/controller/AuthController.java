package com.snut_likeliion.domain.auth.controller;

import com.snut_likeliion.domain.auth.dto.RegisterReq;
import com.snut_likeliion.domain.auth.service.AuthService;
import com.snut_likeliion.global.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Void> register(@RequestBody @Valid RegisterReq req) {
        authService.register(req);
        return ApiResponse.success("회원가입 성공");
    }
}
