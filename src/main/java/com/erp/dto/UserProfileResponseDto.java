package com.erp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileResponseDto {
    private String username;
    private String phone;
    private String email;
    private String department;
    private String position;
    private String address;
    private Double latitude;
    private Double longitude;
}