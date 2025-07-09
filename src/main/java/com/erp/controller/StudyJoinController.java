package com.erp.controller;

import com.erp.dto.UserDto;
import com.erp.service.StudyJoinService;
import com.erp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class StudyJoinController {

    private final StudyJoinService joinService;
    private final UserService userService;

    @PostMapping("/api/study/joinRequest/{studyId}")
    public ResponseEntity<?> requestJoin(@PathVariable Long studyId, Principal principal) {
        UserDto userDto = userService.findByUsername(principal.getName());

        // 요청 처리 결과 메시지 받기
        String message = joinService.requestJoinStudy(studyId, userDto);

        // 메시지에 따라 ResponseEntity 반환
        if (message.equals("스터디 신청이 완료되었습니다.")) {
            return ResponseEntity.ok(Map.of("message", message));  // 성공
        } else {
            return ResponseEntity.badRequest().body(Map.of("message", message));  // 실패
        }
    }

}
