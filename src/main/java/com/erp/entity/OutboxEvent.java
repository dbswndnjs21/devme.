package com.erp.entity;

import com.erp.enums.OutboxEventStatus;
import com.erp.enums.OutboxEventType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
        name = "outbox_events",
        indexes = {
                @Index(name = "idx_outbox_status_next_retry", columnList = "status,nextRetryAt"),
                @Index(name = "idx_outbox_aggregate", columnList = "aggregateType,aggregateId")
        }
)
public class OutboxEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String aggregateType;

    @Column(nullable = false)
    private Long aggregateId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private OutboxEventType eventType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OutboxEventStatus status;

    @Column(nullable = false)
    private Integer payloadVersion;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String payloadJson;

    @Column(nullable = false)
    private Integer retryCount;

    @Column(columnDefinition = "TEXT")
    private String lastError;

    private LocalDateTime nextRetryAt;

    private LocalDateTime processedAt;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        if (status == null) {
            status = OutboxEventStatus.INIT;
        }
        if (payloadVersion == null) {
            payloadVersion = 1;
        }
        if (retryCount == null) {
            retryCount = 0;
        }
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void markProcessing() {
        this.status = OutboxEventStatus.PROCESSING;
        this.lastError = null;
        this.nextRetryAt = null;
    }

    public void markProcessed() {
        this.status = OutboxEventStatus.PROCESSED;
        this.processedAt = LocalDateTime.now();
        this.lastError = null;
    }

    public void markFailed(String errorMessage, LocalDateTime nextRetryAt) {
        this.status = OutboxEventStatus.FAILED;
        this.retryCount = this.retryCount + 1;
        this.lastError = errorMessage;
        this.nextRetryAt = nextRetryAt;
    }

    public void resetForRetry(LocalDateTime nextRetryAt) {
        this.status = OutboxEventStatus.INIT;
        this.nextRetryAt = nextRetryAt;
    }
}
