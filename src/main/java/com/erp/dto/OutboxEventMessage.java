package com.erp.dto;

import com.erp.entity.OutboxEvent;
import com.erp.enums.OutboxEventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutboxEventMessage {
    private Long outboxEventId;
    private String aggregateType;
    private Long aggregateId;
    private OutboxEventType eventType;
    private Integer payloadVersion;
    private String payloadJson;

    public static OutboxEventMessage from(OutboxEvent outboxEvent) {
        return OutboxEventMessage.builder()
                .outboxEventId(outboxEvent.getId())
                .aggregateType(outboxEvent.getAggregateType())
                .aggregateId(outboxEvent.getAggregateId())
                .eventType(outboxEvent.getEventType())
                .payloadVersion(outboxEvent.getPayloadVersion())
                .payloadJson(outboxEvent.getPayloadJson())
                .build();
    }
}
