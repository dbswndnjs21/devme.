package com.erp.domain.dto;

import com.erp.domain.enums.RequestStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class StudyJoinRequestListDto {

    private Long requestId;
    private LocalDateTime createdAt;
    private RequestStatus status;

    private Long studyId;
    private Long userId;

    private String studyTitle;
}
