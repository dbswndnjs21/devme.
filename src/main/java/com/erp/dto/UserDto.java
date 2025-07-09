package com.erp.dto;

import com.erp.entity.User;
import com.erp.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserDto {

    @NotBlank(message = "아이디는 필수입니다")
    private String username;
    
    @NotBlank(message = "비밀번호는 필수입니다")
    private String password;

    private String phone;
    private Role role;
    private String department;
    private String position;

    @NotBlank(message = "이메일은 필수입니다")
    @Email(message = "올바른 이메일 형식이 아닙니다")
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
