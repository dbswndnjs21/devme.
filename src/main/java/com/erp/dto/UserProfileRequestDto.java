package com.erp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileRequestDto {
    private String phone;
    private String email;
    private String department;
    private String position;
    private String address;
    private Double latitude;
    private Double longitude;
}