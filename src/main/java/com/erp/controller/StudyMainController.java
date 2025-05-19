package com.erp.controller;


import com.erp.domain.dto.StudyMainDto;
import com.erp.service.AttendanceService;
import com.erp.service.CustomUserDetails;
import com.erp.service.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class StudyMainController {

    private final StudyService studyService;
    private final AttendanceService attendanceService;

    @GetMapping("/api/study/main/{studyId}")
    public ResponseEntity<StudyMainDto> getStudyMain(@PathVariable Long studyId, @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(studyService.getStudyMainInfo(studyId, user.getId()));
    }

    @GetMapping("/api/study/{studyId}/attendance/check")
    public ResponseEntity<?> checkAttendance(@PathVariable Long studyId, @AuthenticationPrincipal CustomUserDetails user) {
        boolean attended = attendanceService.hasAttendedToday(user.getId(), studyId);
        System.out.println("test : " + attended);
        return ResponseEntity.ok(Map.of("alreadyAttended", attended));
    }
}
