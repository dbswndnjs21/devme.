package com.erp.dto;

import com.erp.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ScheduleDto {
    private Long id;
    private String title;
    private String start; // FullCalendar의 "start" 필드명 요구
    private String description;

    public static ScheduleDto fromEntity(Schedule schedule) {
        return new ScheduleDto(
                schedule.getId(),
                schedule.getTitle(),
                schedule.getDate().toString(),
                schedule.getDescription()
        );
    }
}
