package com.erp.domain.repository;

import com.erp.domain.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // 사용자 알림 리스트 보기
    List<Notification> findByReceiverIdOrderByCreatedAtDesc(Long receiverId);

    List<Notification> findByReceiverIdAndIsReadOrderByCreatedAtDesc(Long receiverId, boolean isRead);

    // 한건 읽음 처리
    @Modifying
    @Query("update Notification n set n.isRead = true where n.id = :id and n.receiverId = :receiverId")
    void markRead(@Param("id") Long id, @Param("receiverId") Long receiverId);

    // 전체 읽음 처리
    @Modifying
    @Query("update Notification n set n.isRead = true where n.receiverId = :receiverId and n.isRead = false")
    void markAllRead(@Param("receiverId") Long receiverId);
}
