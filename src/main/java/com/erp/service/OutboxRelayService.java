package com.erp.service;

import com.erp.config.RabbitMQConfig;
import com.erp.dto.OutboxEventMessage;
import com.erp.entity.OutboxEvent;
import com.erp.enums.OutboxEventStatus;
import com.erp.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxRelayService {

    private static final int MAX_RETRY_COUNT = 3;

    private final OutboxEventRepository outboxEventRepository;
    private final RabbitTemplate rabbitTemplate;

    @Scheduled(fixedDelay = 5000)
    public void relayPendingEvents() {
        List<OutboxEvent> candidates = new ArrayList<>();
        candidates.addAll(outboxEventRepository.findTop20ByStatusAndNextRetryAtIsNullOrderByCreatedAtAsc(
                OutboxEventStatus.INIT
        ));
        candidates.addAll(outboxEventRepository.findTop20ByStatusAndNextRetryAtBeforeOrderByCreatedAtAsc(
                OutboxEventStatus.INIT,
                LocalDateTime.now()
        ));

        for (OutboxEvent event : candidates) {
            publishEvent(event.getId());
        }
    }

    @Transactional
    public void publishEvent(Long outboxEventId) {
        OutboxEvent outboxEvent = outboxEventRepository.findById(outboxEventId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Outbox 이벤트입니다."));

        if (outboxEvent.getStatus() == OutboxEventStatus.PROCESSED) {
            return;
        }

        if (outboxEvent.getNextRetryAt() != null && outboxEvent.getNextRetryAt().isAfter(LocalDateTime.now())) {
            return;
        }

        try {
            outboxEvent.markProcessing();
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.STUDY_OUTBOX_EXCHANGE_NAME,
                    RabbitMQConfig.STUDY_OUTBOX_ROUTING_KEY,
                    OutboxEventMessage.from(outboxEvent)
            );
            outboxEvent.markProcessed();
            outboxEventRepository.save(outboxEvent);
            log.info("Outbox 이벤트 발행 완료: id={}, type={}, aggregateId={}",
                    outboxEvent.getId(), outboxEvent.getEventType(), outboxEvent.getAggregateId());
        } catch (Exception e) {
            handleFailure(outboxEvent, e);
        }
    }

    private void handleFailure(OutboxEvent outboxEvent, Exception e) {
        LocalDateTime nextRetryAt = LocalDateTime.now().plusMinutes(Math.min(outboxEvent.getRetryCount() + 1, 30));
        outboxEvent.markFailed(truncate(e.getMessage()), nextRetryAt);

        if (outboxEvent.getRetryCount() < MAX_RETRY_COUNT) {
            outboxEvent.resetForRetry(nextRetryAt);
        }

        outboxEventRepository.save(outboxEvent);

        log.error("Outbox 이벤트 발행 실패: id={}, retryCount={}, nextRetryAt={}",
                outboxEvent.getId(), outboxEvent.getRetryCount(), outboxEvent.getNextRetryAt(), e);
    }

    private String truncate(String message) {
        if (message == null || message.length() <= 1000) {
            return message;
        }
        return message.substring(0, 1000);
    }
}
