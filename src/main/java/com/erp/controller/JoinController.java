package com.erp.controller;

import com.erp.domain.dto.UserDto;
import com.erp.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@AllArgsConstructor
public class JoinController {

    private UserService userService;

    @GetMapping("/join")
    public String joinForm() {
        return "join";
    }

    @PostMapping("/api/join")
    @ResponseBody
    public ResponseEntity<Map<String,Object>> joinUser(@RequestBody UserDto userDTO) {
        Map<String, Object> response = new HashMap<>();
        try {
            userService.join(userDTO);  // 회원가입 처리
            response.put("success", true);  // 성공
            response.put("message", "회원가입 성공");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // 예외가 발생하면 실패 처리
            response.put("success", false);
            response.put("message", "회원가입 실패: " + e.getMessage());  // 실패 원인 메시지 추가
            return ResponseEntity.status(500).body(response);  // 실패 시 500 서버 에러 상태 코드 반환
        }

    }
}
