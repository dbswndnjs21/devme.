package com.erp.controller;

import com.erp.domain.dto.HomeUserInfo;
import com.erp.service.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
public class HomeController {

    @GetMapping("/")
    public String home(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if (customUserDetails != null) {
            log.info("세션이름 : {}", customUserDetails.getUsername());
        } else {
            log.info("로그인하지 않은 사용자입니다.");
        }
        return "home"; // templates/home.html
    }

    @GetMapping("/home")
    @ResponseBody
    public ResponseEntity<HomeUserInfo> getUserInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if (customUserDetails == null) {
            return ResponseEntity.status(401).build(); // 비로그인 상태 처리
        }
        HomeUserInfo homeUserInfo = new HomeUserInfo(customUserDetails.getUsername(), customUserDetails.getPosition(),customUserDetails.getDepartment());
        return ResponseEntity.ok(homeUserInfo);
    }
}
