package com.snut_likelion.domain.user.controller;

import com.snut_likelion.domain.user.dto.response.SayingResponse;
import com.snut_likelion.domain.user.service.SayingService;
import com.snut_likelion.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sayings")
@RequiredArgsConstructor
public class SayingController {

    private final SayingService sayingService;

    @GetMapping
    public ApiResponse<List<SayingResponse>> getSayings() {
        return ApiResponse.success(
                sayingService.getSayings(),
                "명언 리스트 조회 성공"
        );
    }
}
