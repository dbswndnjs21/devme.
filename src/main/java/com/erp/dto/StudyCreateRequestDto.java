package com.erp.dto;

import com.erp.enums.StudyType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StudyCreateRequestDto {
    private String name;
    private String description;
    private int maxMembers;

    private String goal;
    private String howToProceed;
    private String tools;
    private String rules;
    private String schedule;
    private String baseAddress;   // 좌표 변환용
    private String detailAddress; // DB 저장용
    private StudyType studyType;
}
