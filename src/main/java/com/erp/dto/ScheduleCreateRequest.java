package com.erp.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ScheduleCreateRequest {
    private String title;
    private String description;
    private LocalDate date; // yyyy-MM-dd 형식
}
