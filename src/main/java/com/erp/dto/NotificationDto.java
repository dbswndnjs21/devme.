package com.erp.dto;

import com.erp.entity.Notification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationDto {

    private Long id;
    private Long receiverId;
    private String message;
    private boolean isRead;
    private LocalDateTime createdAt;

    public NotificationDto(Notification notification) {
        this.id = notification.getId();
        this.receiverId = notification.getReceiverId();
        this.message = notification.getMessage();
        this.isRead = notification.isRead();
        this.createdAt = notification.getCreatedAt();
    }

    public Notification toEntity() {
        return Notification.builder()
                .id(this.id)
                .receiverId(this.receiverId)
                .message(this.message)
                .isRead(this.isRead)
                .createdAt(this.createdAt)
                .build();
    }
}
