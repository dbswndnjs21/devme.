package com.erp.service;

import com.erp.config.RabbitMQConfig;
import com.erp.domain.dto.NotificationMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final RabbitTemplate rabbitTemplate;

    // (스터디장 ID, "알림 내용");
    public void sendNotification(Long receiverId, String content) {
        NotificationMessage message = new NotificationMessage(receiverId, content);
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.EXCHANGE_NAME,
            RabbitMQConfig.ROUTING_KEY,
            message
        );
    }
}
