
package com.erp.dto;

import com.erp.enums.StudyStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

// 근처 스터디 조회 요청 DTO
@Getter @Setter
@NoArgsConstructor
public class NearbyStudyRequest {
    
    @NotNull
    private Long userId;
    
    @DecimalMin(value = "0.1", message = "반경은 최소 0.1km 이상이어야 합니다")
    @DecimalMax(value = "50.0", message = "반경은 최대 50km 이하여야 합니다")
    private Double radiusKm = 5.0; // 기본값 5km
    
    private StudyStatus status = StudyStatus.INPROGRESS; // 기본값: 진행중
}