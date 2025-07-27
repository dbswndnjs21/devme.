package com.erp.controller;


import com.erp.dto.ApiResponse;
import com.erp.dto.ScheduleCreateRequest;
import com.erp.dto.ScheduleDto;
import com.erp.dto.StudyMainDto;
import com.erp.service.AttendanceService;
import com.erp.service.CustomUserDetails;
import com.erp.service.ScheduleService;
import com.erp.service.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class StudyMainController {

    private final StudyService studyService;
    private final AttendanceService attendanceService;
    private final ScheduleService scheduleService;

    @GetMapping("/api/study/main/{studyId}")
    public ResponseEntity<StudyMainDto> getStudyMain(@PathVariable Long studyId, @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(studyService.getStudyMainInfo(studyId, user.getId()));
    }

    @GetMapping("/api/study/{studyId}/attendance/check")
    public ResponseEntity<?> checkAttendance(@PathVariable Long studyId, @AuthenticationPrincipal CustomUserDetails user) {
        boolean attended = attendanceService.hasAttendedToday(user.getId(), studyId);
        return ResponseEntity.ok(Map.of("alreadyAttended", attended));
    }

    @GetMapping("/api/study/{studyId}/schedules")
    @ResponseBody
    public ApiResponse<List<ScheduleDto>> getSchedules(@PathVariable Long studyId) {
        List<ScheduleDto> schedules = scheduleService.getSchedulesByStudyId(studyId);
        return ApiResponse.success(schedules);
    }

//
//
    @PostMapping("/api/study/{studyId}/schedule")
    @ResponseBody
    public ApiResponse<Void> createSchedule(@PathVariable Long studyId,
                                            @RequestBody ScheduleCreateRequest request,
                                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        scheduleService.createSchedule(studyId, request, userDetails.getId());
        return ApiResponse.success("일정이 등록되었습니다.", null);
    }

}
