package com.erp.listener;

import com.erp.config.RabbitMQConfig;
import com.erp.document.StudyDocument;
import com.erp.dto.OutboxEventMessage;
import com.erp.dto.StudyOutboxPayload;
import com.erp.enums.OutboxEventType;
import com.erp.repository.StudySearchRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class StudySearchIndexListener {

    private final StudySearchRepository studySearchRepository;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = RabbitMQConfig.STUDY_OUTBOX_QUEUE_NAME)
    public void handleStudyOutboxMessage(OutboxEventMessage message) {
        try {
            if (message.getEventType() == OutboxEventType.STUDY_DELETED) {
                studySearchRepository.deleteById(String.valueOf(message.getAggregateId()));
                log.info("Elasticsearch 문서 삭제 완료: studyId={}", message.getAggregateId());
                return;
            }

            StudyOutboxPayload payload = objectMapper.readValue(message.getPayloadJson(), StudyOutboxPayload.class);
            StudyDocument document = StudyDocument.fromPayload(payload);
            studySearchRepository.save(document);

            log.info("Elasticsearch 문서 upsert 완료: studyId={}, eventType={}",
                    payload.getStudyId(), message.getEventType());
        } catch (Exception e) {
            log.error("Elasticsearch 인덱싱 처리 실패: outboxEventId={}", message.getOutboxEventId(), e);
            throw new IllegalStateException("Elasticsearch indexing failed", e);
        }
    }
}
