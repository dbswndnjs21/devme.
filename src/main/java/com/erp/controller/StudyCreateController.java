package com.erp.controller;

import com.erp.domain.dto.StudyCreateRequestDto;
import com.erp.service.CustomUserDetails;
import com.erp.service.StudyCreateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class StudyCreateController {

    private final StudyCreateService studyCreateService;

    @GetMapping("/studyCreate")
    public String studyCreateForm(){
        return "studyCreate";
    }

    @PostMapping("/api/studyCreate")
    public ResponseEntity<?> studyCreate(@RequestBody StudyCreateRequestDto dto,
                                         @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        studyCreateService.studyCreate(dto, customUserDetails.getUser());
        return ResponseEntity.ok("스터디가 성공적으로 생성되었습니다");
    }
}
