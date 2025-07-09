package com.erp.dto;

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
}
