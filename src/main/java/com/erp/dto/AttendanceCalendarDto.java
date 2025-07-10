package com.erp.dto;

import com.erp.entity.Attendance;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AttendanceCalendarDto {
    private Long studyId;
    private String title;
    private String start;
    private String color;

    public AttendanceCalendarDto(Attendance att) {
        this.studyId = att.getStudy().getId();
        this.title = "출석 - " + att.getStudy().getTitle(); // optional
        this.start = att.getDate().toString();
        this.color = "#4CAF50"; // 나중에 스터디별 색상 지정도 가능
    }
}