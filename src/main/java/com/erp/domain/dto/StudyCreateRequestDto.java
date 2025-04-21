package com.erp.domain.dto;

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
}
