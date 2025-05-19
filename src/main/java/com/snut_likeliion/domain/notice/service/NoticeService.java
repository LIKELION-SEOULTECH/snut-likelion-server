package com.snut_likeliion.domain.notice.service;


import com.snut_likeliion.domain.notice.dto.CreateNoticeRequest;
import com.snut_likeliion.domain.notice.dto.NoticeDetailResponse;
import com.snut_likeliion.domain.notice.dto.NoticeListResponse;
import com.snut_likeliion.domain.notice.dto.UpdateNoticeRequest;
import com.snut_likeliion.domain.notice.entity.Notice;
import com.snut_likeliion.domain.notice.exception.NoticeErrorCode;
import com.snut_likeliion.domain.notice.repository.NoticeRepository;
import com.snut_likeliion.global.error.GlobalErrorCode;
import com.snut_likeliion.global.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
        return noticeRepository.findAll().stream()
                .map(NoticeListResponse::from)
                .toList();
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