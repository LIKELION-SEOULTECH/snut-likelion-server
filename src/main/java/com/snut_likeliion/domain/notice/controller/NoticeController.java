package com.snut_likeliion.domain.notice.controller;

import com.snut_likeliion.domain.notice.dto.*;
import com.snut_likeliion.domain.notice.service.NoticeService;
import com.snut_likeliion.global.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Long> createNotice(
            @Valid @RequestBody CreateNoticeRequest request) {

        Long noticeId = noticeService.createNotice(request);

        return ApiResponse.success(noticeId);
    }

    @PatchMapping("/{noticeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateNotice(
            @PathVariable Long noticeId,
            @Valid @RequestBody UpdateNoticeRequest request) {

        noticeService.updateNotice(noticeId, request);
    }

    @DeleteMapping("/{noticeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNotice(@PathVariable Long noticeId) {

        noticeService.deleteNotice(noticeId);
    }

    @GetMapping
    public ApiResponse<NoticePageResponse> getNoticePage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {

        return ApiResponse.success(noticeService.getNoticePage(page, size, keyword));
    }

    @GetMapping("/{noticeId}")
    public ApiResponse<NoticeDetailResponse> getNotice(@PathVariable Long noticeId) {

        return ApiResponse.success(noticeService.getNoticeDetail(noticeId));
    }
}