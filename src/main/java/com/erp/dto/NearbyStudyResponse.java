package com.erp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

// 근처 스터디 응답 DTO
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NearbyStudyResponse {
    
    private UserLocationDto userLocation;
    private List<StudyWithDistanceDto> studies;
    private Double radiusKm;
    private Integer totalCount;
    
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserLocationDto {
        private Double latitude;
        private Double longitude;
        private String address;
    }
}