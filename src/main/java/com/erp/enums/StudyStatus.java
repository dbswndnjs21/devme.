package com.erp.enums;

import lombok.Getter;

@Getter
public enum StudyStatus {

    INPROGRESS("진행중"),
    COMPLETED("종료");

    private final String displayName;

    StudyStatus(String displayName) {
        this.displayName = displayName;
    }
}
