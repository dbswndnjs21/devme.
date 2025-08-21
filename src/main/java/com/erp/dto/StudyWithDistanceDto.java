package com.erp.dto;

import com.erp.enums.StudyStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor
public class StudyWithDistanceDto {

    private Long id;
    private String title;
    private String description;
    private int maxMembers;
    private StudyStatus status;
    private String createdByUsername;
    private Timestamp createdAt;
    private String locationAddress;
    private Double locationLatitude;
    private Double locationLongitude;
    private Double distanceKm; // 사용자로부터의 거리
    private int currentMemberCount = 0; // 기본값 0

    // Native Query용 생성자: status는 String으로 받아서 Enum으로 변환
    public StudyWithDistanceDto(Long id, String title, String description,
                                int maxMembers, String status,
                                String createdByUsername, Timestamp createdAt,
                                String locationAddress, Double locationLatitude,
                                Double locationLongitude, Double distanceKm) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.maxMembers = maxMembers;
        this.status = StudyStatus.valueOf(status); // String -> Enum 변환
        this.createdByUsername = createdByUsername;
        this.createdAt = createdAt;
        this.locationAddress = locationAddress;
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
        this.distanceKm = distanceKm;
    }
}
