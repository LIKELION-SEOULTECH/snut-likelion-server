package com.snut_likelion.domain.notice.service;


import com.snut_likelion.domain.notice.dto.event.NoticeCreatedEvent;
import com.snut_likelion.domain.notice.dto.request.CreateNoticeRequest;
import com.snut_likelion.domain.notice.dto.request.UpdateNoticeRequest;
import com.snut_likelion.domain.notice.dto.response.NoticeDetailResponse;
import com.snut_likelion.domain.notice.dto.response.NoticeListResponse;
import com.snut_likelion.domain.notice.dto.response.NoticePageResponse;
import com.snut_likelion.domain.notice.entity.Notice;
import com.snut_likelion.domain.notice.exception.NoticeErrorCode;
import com.snut_likelion.domain.notice.repository.NoticeRepository;
import com.snut_likelion.global.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final ApplicationEventPublisher publisher;

    @Transactional
    public Long createNotice(CreateNoticeRequest request) {
        Notice notice = noticeRepository.save(request.toEntity());
        publisher.publishEvent(new NoticeCreatedEvent(notice.getId(), notice.getContent()));
        return notice.getId();
    }

    @Transactional(readOnly = true)
    public NoticePageResponse getNoticePage(int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Notice> noticePage = (keyword == null || keyword.isBlank())
                ? noticeRepository.findAllByOrderByPinnedDescUpdatedAtDesc(pageable)
                : noticeRepository.findByTitleContainingIgnoreCaseOrderByPinnedDescUpdatedAtDesc(keyword, pageable);

        List<NoticeListResponse> content = noticePage.getContent()
                .stream()
                .map(NoticeListResponse::from)
                .toList();

        return NoticePageResponse.of(content, noticePage);
    }

    @Transactional(readOnly = true)
    public NoticeDetailResponse getNoticeDetail(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NoticeErrorCode.NOT_FOUND));

        return NoticeDetailResponse.from(notice);
    }

    @Transactional
    public void updateNotice(Long id, UpdateNoticeRequest request) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NoticeErrorCode.NOT_FOUND));

        notice.update(
                request.getTitle(),
                request.getContent(),
                request.getPinned()
        );
    }

    @Transactional
    public void deleteNotice(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NoticeErrorCode.NOT_FOUND));

        noticeRepository.delete(notice);
    }
}