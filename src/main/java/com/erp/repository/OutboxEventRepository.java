package com.erp.repository;

import com.erp.entity.OutboxEvent;
import com.erp.enums.OutboxEventStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {
    long countByStatus(OutboxEventStatus status);

    List<OutboxEvent> findTop20ByStatusAndNextRetryAtBeforeOrderByCreatedAtAsc(
            OutboxEventStatus status,
            LocalDateTime nextRetryAt
    );

    List<OutboxEvent> findTop20ByStatusAndNextRetryAtIsNullOrderByCreatedAtAsc(OutboxEventStatus status);
}
