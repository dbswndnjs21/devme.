package com.erp.dto;

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
    private String createdByUsername;
    private LocalDateTime createdAt;
    private String status;
    private String role;

    // 챗봇용 추가 필드
    private String locationAddress;
    private Double locationLatitude;
    private Double locationLongitude;
    private Double distance; // km 단위 거리
}
