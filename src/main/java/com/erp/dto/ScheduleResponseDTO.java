package com.erp.dto;

import com.erp.entity.Schedule;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class ScheduleResponseDTO {
    private String title;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private String color;

    public static ScheduleResponseDTO fromEntity(Schedule schedule) {
        return new ScheduleResponseDTO(
            schedule.getTitle(),
            schedule.getDescription(),
            schedule.getDate(),
            "#2196f3" // 파랑색
        );
    }
}
