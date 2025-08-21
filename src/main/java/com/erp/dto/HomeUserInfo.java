package com.erp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class HomeUserInfo {
    private Long id;
    private String username;
    private String position;
    private String department;
    private String address;
    private Double latitude;
    private Double longitude;
}
