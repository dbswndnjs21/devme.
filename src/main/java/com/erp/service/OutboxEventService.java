package com.erp.service;

import com.erp.dto.StudyOutboxPayload;
import com.erp.entity.OutboxEvent;
import com.erp.enums.OutboxEventStatus;
import com.erp.enums.OutboxEventType;
import com.erp.repository.OutboxEventRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OutboxEventService {

    private static final String STUDY_AGGREGATE_TYPE = "STUDY";
    private static final int CURRENT_PAYLOAD_VERSION = 1;

    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    public void saveStudyCreatedEvent(StudyOutboxPayload payload) {
        saveEvent(
                STUDY_AGGREGATE_TYPE,
                payload.getStudyId(),
                OutboxEventType.STUDY_CREATED,
                payload
        );
    }

    private void saveEvent(String aggregateType, Long aggregateId, OutboxEventType eventType, Object payload) {
        try {
            OutboxEvent outboxEvent = OutboxEvent.builder()
                    .aggregateType(aggregateType)
                    .aggregateId(aggregateId)
                    .eventType(eventType)
                    .status(OutboxEventStatus.INIT)
                    .payloadVersion(CURRENT_PAYLOAD_VERSION)
                    .payloadJson(objectMapper.writeValueAsString(payload))
                    .retryCount(0)
                    .build();
            outboxEventRepository.save(outboxEvent);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Outbox payload serialization failed", e);
        }
    }
}
