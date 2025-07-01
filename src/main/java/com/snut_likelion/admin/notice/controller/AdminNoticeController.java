package com.snut_likelion.admin.notice.controller;

import com.snut_likelion.admin.notice.dto.response.NoticePageResponse;
import com.snut_likelion.admin.notice.service.AdminNoticeService;
import com.snut_likelion.domain.notice.dto.CreateNoticeRequest;
import com.snut_likelion.domain.notice.dto.UpdateNoticeRequest;
import com.snut_likelion.global.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/notices")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_MANAGER')")
public class AdminNoticeController {

    private final AdminNoticeService adminNoticeService;

    // 목록
    @GetMapping
    public ApiResponse<NoticePageResponse> getNoticeList(
            @RequestParam(value = "page",  defaultValue = "0")  int page,
            @RequestParam(value = "keyword", required = false) String keyword
    ) {
        return ApiResponse.success(
                adminNoticeService.getNoticeList(page, keyword),
                "공지 리스트 조회 성공"
        );
    }

    // 공지 생성
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Object> createNotice(
            @Valid @RequestBody CreateNoticeRequest req
    ) {
        adminNoticeService.create(req);
        return ApiResponse.success("공지 생성 성공");
    }

    // 공지 수정
    @PatchMapping("/{noticeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void modifyNotice(
            @PathVariable Long noticeId,
            @Valid @RequestBody UpdateNoticeRequest req
    ) {
        adminNoticeService.modify(noticeId, req);
    }

    // 핀 고정/해제
    @PatchMapping("/{noticeId}/pin")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void togglePin(@PathVariable Long noticeId) {
        adminNoticeService.togglePin(noticeId);
    }

    // 단건 삭제
    @DeleteMapping("/{noticeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNotice(@PathVariable Long noticeId) {
        adminNoticeService.remove(noticeId);
    }

    // 다중 삭제
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNotices(@RequestParam("ids") List<Long> ids) {
        adminNoticeService.removeNotices(ids);
    }
}
