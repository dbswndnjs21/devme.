package com.erp.domain.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class StudyDto {
    private Long id;
    private String title;
    private String description;
    private int maxMembers;
    private Long createdById;
    private String createdByUsername; // 필요하면 추가
    private LocalDateTime createdAt;
    private String status;
}
