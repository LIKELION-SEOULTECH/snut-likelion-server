package com.snut_likelion.domain.notice.listener;

import com.snut_likelion.domain.notice.dto.event.NoticeCreatedEvent;
import com.snut_likelion.domain.notice.service.NoticeSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class NoticeCreatedListener {
    private final NoticeSummaryService summaryService;

    /**
     * 메인 트랜잭션이 커밋된 이후에만 실행
     * (= 메인 저장 트랜잭션이 롤백되면 이 리스너는 호출되지 않음)
     *
     * @param event
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onNoticeCreated(NoticeCreatedEvent event) {
        summaryService.generateAndSaveSummary(event.getNoticeId(), event.getContent());
    }
}