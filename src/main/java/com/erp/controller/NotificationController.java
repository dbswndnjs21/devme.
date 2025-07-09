package com.erp.controller;

import com.erp.dto.ApiResponse;
import com.erp.dto.NotificationDto;
import com.erp.service.CustomUserDetails;
import com.erp.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("notifications")
    public String notificationForm() {
        return "notifications";
    }

    @GetMapping("/api/notifications")
    @ResponseBody
    public ApiResponse<List<NotificationDto>> notifications(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<NotificationDto> notifications = notificationService.getNotifications(customUserDetails.getId());
        return ApiResponse.success(notifications);
    }

    /** 단일 읽음 */
    @PatchMapping("/api/notifications/{id}/read")
    @ResponseBody
    public ApiResponse<Void> read(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails user) {
        notificationService.markRead(user.getId(), id);
        return ApiResponse.success();
    }

    /** 모두 읽음 */
    @PatchMapping("/api/notifications/read-all")
    @ResponseBody
    public ApiResponse<Void> readAll(@AuthenticationPrincipal CustomUserDetails user) {
        notificationService.markAllRead(user.getId());
        return ApiResponse.success();
    }
}
