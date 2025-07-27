package com.erp.controller;

import com.erp.dto.ApiResponse;
import com.erp.dto.ScheduleResponseDTO;
import com.erp.service.CustomUserDetails;
import com.erp.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping("/api/mySchedule")
    @ResponseBody
    public ApiResponse<List<ScheduleResponseDTO>> getMySchedules(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getId();
        List<ScheduleResponseDTO> schedules = scheduleService.getSchedulesByUser(userId);
        return ApiResponse.success("일정 불러오기 성공", schedules);
    }

}
