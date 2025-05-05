package com.snut_likeliion.domain.notice.controller;

import com.snut_likeliion.domain.notice.dto.CreateNoticeRequest;
import com.snut_likeliion.domain.notice.dto.NoticeDetailResponse;
import com.snut_likeliion.domain.notice.dto.NoticeListResponse;
import com.snut_likeliion.domain.notice.dto.UpdateNoticeRequest;
import com.snut_likeliion.domain.notice.service.NoticeService;
import com.snut_likeliion.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @PostMapping
    public ResponseEntity<ApiResponse<Long>> createNotice(@RequestBody CreateNoticeRequest request) {
        Long noticeId = noticeService.createNotice(request);

        return ResponseEntity.ok(ApiResponse.success(noticeId));
    }

    @PatchMapping("/{noticeId}")
    public ResponseEntity<ApiResponse<Void>> updateNotice(
            @PathVariable Long noticeId,
            @RequestBody UpdateNoticeRequest request) {

        noticeService.updateNotice(noticeId, request);

        return ResponseEntity.ok(ApiResponse.success((Void) null));
    }

    @DeleteMapping("/{noticeId}")
    public ResponseEntity<ApiResponse<Void>> deleteNotice(@PathVariable Long noticeId) {
        noticeService.deleteNotice(noticeId);

        return ResponseEntity.ok(ApiResponse.success((Void) null));
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
