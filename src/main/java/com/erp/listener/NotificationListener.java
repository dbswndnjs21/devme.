package com.erp.listener;

import com.erp.config.RabbitMQConfig;
import com.erp.domain.dto.NotificationMessage;
import com.erp.domain.entity.Notification;
import com.erp.domain.repository.NotificationRepository;
import com.erp.domain.repository.UserRepository;
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
    private final UserRepository userRepository;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receiveMessage(NotificationMessage message) {

        System.out.println("Rabbit message 수신 :  " + message);
        // 1. DB 저장
        Notification notification = Notification.builder().
                receiverId(message.getReceiverId()).
                message(message.getContent()).
                build();
        notificationRepository.save(notification);

        String usernameById = userRepository.findUsernameById(message.getReceiverId());


        // 2. WebSocket 전송
//        messagingTemplate.convertAndSendToUser(
////            String.valueOf(message.getReceiverId()),
//                usernameById,
//            "/queue/notifications",
//            message.getContent()
//        );
        messagingTemplate.convertAndSend(
                "/topic/notifications." + usernameById,
                message.getContent()
        ); ///topic/notifications.11

        System.out.println("WebSocket 메시지 전송 완료 to user: " + usernameById);
        System.out.println("WebSocket 메시지 : " + message.getContent());
    }
}
