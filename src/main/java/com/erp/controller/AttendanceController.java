package com.erp.controller;

import com.erp.dto.ApiResponse;
import com.erp.dto.AttendanceCalendarDto;
import com.erp.service.AttendanceService;
import com.erp.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("/api/study/{studyId}/attendance")
    public ResponseEntity<?> attend(@PathVariable Long studyId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            attendanceService.markAttendance(userDetails.getId(), studyId);
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("출석 실패: " + e.getMessage());
        }
    }

    @GetMapping("/api/myAttendance")
    public ApiResponse<List<AttendanceCalendarDto>> getMyAttendance(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<AttendanceCalendarDto> myAttendance = attendanceService.getMyAttendance(userDetails.getId());
        return ApiResponse.success(myAttendance);
    }
}
