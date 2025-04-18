package com.erp.domain.dto;

import com.erp.domain.entity.User;
import com.erp.domain.enums.Role;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private String username;
    private String password;
    private String phone;
    private Role role;
    private String department;
    private String position;

    public User toEntity(String encodedPassword) {
        return User.builder()
                .username(username)
                .password(encodedPassword) // 암호화된 비밀번호를 넣어줘야 함
                .phone(phone)
                .role(role)
                .department(department)
                .position(position)
                .build();
    }

    public UserDto(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.phone = user.getPhone();
        this.role = user.getRole();
        this.department = user.getDepartment();
        this.position = user.getPosition();
    }
}
