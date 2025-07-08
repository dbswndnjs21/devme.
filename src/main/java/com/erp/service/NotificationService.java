package com.erp.service;

import com.erp.config.RabbitMQConfig;
import com.erp.domain.dto.NotificationDto;
import com.erp.domain.dto.NotificationMessage;
import com.erp.domain.entity.Notification;
import com.erp.domain.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final RabbitTemplate rabbitTemplate;
    private final NotificationRepository notificationRepository;

    // (스터디장 ID, "알림 내용");
    public void sendNotification(Long receiverId, String content) {
        NotificationMessage message = new NotificationMessage(receiverId, content);
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.EXCHANGE_NAME,
            RabbitMQConfig.ROUTING_KEY,
            message
        );
    }

    public List<NotificationDto> getNotifications(Long userId) {
        List<Notification> notifications = notificationRepository.findByReceiverIdAndIsReadOrderByCreatedAtDesc(userId, false);
        return notifications.stream()
                .map(NotificationDto::new).toList();
    }

    @Transactional
    public void markRead(Long receiverId, Long id) {
        notificationRepository.markRead(id, receiverId);
    }

    @Transactional
    public void markAllRead(Long receiverId) {
        notificationRepository.markAllRead(receiverId);
    }
}
