package com.snut_likelion.domain.notice.controller;

import com.snut_likelion.domain.notice.dto.CreateNoticeRequest;
import com.snut_likelion.domain.notice.dto.NoticeDetailResponse;
import com.snut_likelion.domain.notice.dto.NoticePageResponse;
import com.snut_likelion.domain.notice.dto.UpdateNoticeRequest;
import com.snut_likelion.domain.notice.service.NoticeService;
import com.snut_likelion.global.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ApiResponse<Long> createNotice(
            @Valid @RequestBody CreateNoticeRequest request) {
        return ApiResponse.success(noticeService.createNotice(request));
    }

    @PatchMapping("/{noticeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public void updateNotice(
            @PathVariable Long noticeId,
            @Valid @RequestBody UpdateNoticeRequest request) {
        noticeService.updateNotice(noticeId, request);
    }

    @DeleteMapping("/{noticeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_MANAGER')")
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