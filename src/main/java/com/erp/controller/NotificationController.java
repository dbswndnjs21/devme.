package com.erp.controller;

import com.erp.domain.dto.ApiResponse;
import com.erp.domain.dto.NotificationDto;
import com.erp.service.CustomUserDetails;
import com.erp.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
}
