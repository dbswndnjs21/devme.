package com.erp.controller;

import com.erp.dto.ChatRequest;
import com.erp.dto.ChatResponse;
import com.erp.service.ChatbotService;
import com.erp.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chatbot")
@RequiredArgsConstructor
@Slf4j
public class ChatbotController {

    private final ChatbotService chatbotService;

    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(
            @RequestBody ChatRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        
        log.info("챗봇 요청 - 사용자: {}, 메시지: {}", 
                userDetails != null ? userDetails.getUsername() : "비로그인", 
                request.getMessage());
        
        try {
            ChatResponse response = chatbotService.processMessage(
                    request.getMessage(),
                    request.getLatitude(),
                    request.getLongitude(),
                    userDetails
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("챗봇 처리 중 오류 발생", e);
            return ResponseEntity.ok(ChatResponse.builder()
                    .message("죄송합니다. 일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요.")
                    .build());
        }
    }
}