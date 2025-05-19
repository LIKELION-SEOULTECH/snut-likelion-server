package com.snut_likeliion.domain.notice.controller;

import com.snut_likeliion.domain.notice.dto.CreateNoticeRequest;
import com.snut_likeliion.domain.notice.dto.NoticeDetailResponse;
import com.snut_likeliion.domain.notice.dto.NoticeListResponse;
import com.snut_likeliion.domain.notice.dto.UpdateNoticeRequest;
import com.snut_likeliion.domain.notice.service.NoticeService;
import com.snut_likeliion.global.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<ApiResponse<List<NoticeListResponse>>> getNotices() {
        List<NoticeListResponse> responseList = noticeService.getNoticeList();

        return ResponseEntity.ok(ApiResponse.success(responseList));
    }

    @GetMapping("/{noticeId}")
    public ResponseEntity<ApiResponse<NoticeDetailResponse>> getNotice(@PathVariable Long noticeId) {
        NoticeDetailResponse response = noticeService.getNoticeDetail(noticeId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
