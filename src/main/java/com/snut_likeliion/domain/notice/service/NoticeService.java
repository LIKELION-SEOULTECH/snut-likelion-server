package com.snut_likeliion.domain.notice.service;


import com.snut_likeliion.domain.notice.dto.*;
import com.snut_likeliion.domain.notice.entity.Notice;
import com.snut_likeliion.domain.notice.exception.NoticeErrorCode;
import com.snut_likeliion.domain.notice.repository.NoticeRepository;
import com.snut_likeliion.global.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
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

    @Transactional
    public Long createNotice(CreateNoticeRequest request) {
        Notice notice = request.toEntity();

        return noticeRepository.save(notice).getId();
    }

    @Transactional(readOnly = true)
    public List<NoticeListResponse> getNoticeList() {
        // page = 0, size = Integer.MAX_VALUE, keyword = null
        NoticePageResponse pageResponse = getNoticePage(0, Integer.MAX_VALUE, null);

        return pageResponse.getContent();
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