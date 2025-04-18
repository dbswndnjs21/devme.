package com.erp.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
public class HomeUserInfo {
    private String username;
    private String position;
    private String department;
}
