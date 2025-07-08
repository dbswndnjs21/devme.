package com.erp.domain.repository;

import com.erp.domain.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // 사용자 알림 리스트 보기
    List<Notification> findByReceiverIdOrderByCreatedAtDesc(Long receiverId);

    List<Notification> findByReceiverIdAndIsReadOrderByCreatedAtDesc(Long receiverId, boolean isRead);
}
