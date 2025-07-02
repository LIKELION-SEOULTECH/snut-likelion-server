package com.snut_likelion.domain.notice.service;

import com.snut_likelion.domain.notice.entity.Notice;
import com.snut_likelion.domain.notice.exception.NoticeErrorCode;
import com.snut_likelion.domain.notice.repository.NoticeRepository;
import com.snut_likelion.domain.notice.repository.SummaryApiClient;
import com.snut_likelion.global.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeSummaryService {

    private final NoticeRepository noticeRepository;
    private final SummaryApiClient summaryApiClient;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void generateAndSaveSummary(Long noticeId, String content) {
        try {
            String summary = summaryApiClient.summarize(content);
            Notice notice = noticeRepository.findById(noticeId)
                    .orElseThrow(() -> new NotFoundException(NoticeErrorCode.NOT_FOUND));
            notice.setSummary(summary);
        } catch (Exception ex) {
            // 예외 무시: 요약 실패해도 공지 등록에는 영향 없음
            log.warn("요약 API 호출 실패 (noticeId={}): {}", noticeId, ex.getMessage());
        }
    }
}
