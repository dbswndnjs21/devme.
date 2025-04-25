package com.erp.domain.dto;

import com.erp.domain.entity.User;
import com.erp.domain.enums.Role;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserDto {

    private String username;
    private String password;
    private String phone;
    private Role role;
    private String department;
    private String position;
    private String email;
    private LocalDateTime createdAt;

    public User toEntity() {
        return User.builder()
                .username(username)
                .password(password)
                .phone(phone)
                .role(role)
                .department(department)
                .position(position)
                .email(email)
                .createdAt(createdAt)
                .build();
    }

    public UserDto(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.phone = user.getPhone();
        this.role = user.getRole();
        this.department = user.getDepartment();
        this.position = user.getPosition();
        this.email = user.getEmail();
        this.createdAt = user.getCreatedAt();
    }
}
