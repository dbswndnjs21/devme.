package com.erp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationMessage {
    private Long receiverId;  // 받을 사람 ID
    private String content;   // 알림 내용

}
