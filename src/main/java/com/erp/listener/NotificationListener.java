package com.erp.listener;

import com.erp.config.RabbitMQConfig;
import com.erp.domain.dto.NotificationMessage;
import com.erp.domain.entity.Notification;
import com.erp.domain.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

// NotificationListener.java
@Component
@RequiredArgsConstructor
public class NotificationListener {

    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receiveMessage(NotificationMessage message) {
        // 1. DB 저장
        Notification notification = Notification.builder().
                receiverId(message.getReceiverId()).
                message(message.getContent()).
                build();
        notificationRepository.save(notification);

        // 2. WebSocket 전송
        messagingTemplate.convertAndSendToUser(
            String.valueOf(message.getReceiverId()),
            "/queue/notifications",
            message.getContent()
        );
    }
}
