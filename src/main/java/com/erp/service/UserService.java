package com.erp.service;

import com.erp.dto.UserDto;
import com.erp.entity.User;
import com.erp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public void join(UserDto userDTO) {

        // 1. 아이디 중복 검사 (null 체크 방식)
        if (userRepository.findByUsername(userDTO.getUsername()) != null) {
            throw new IllegalStateException("이미 사용 중인 아이디입니다.");
        }

        // 2. 비밀번호 공백 검사 (DTO 검증도 있지만, 이중 방어 가능)
        if (userDTO.getPassword() == null || userDTO.getPassword().trim().isEmpty()) {
            throw new IllegalStateException("비밀번호는 필수 입력값입니다.");
        }

        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());

        User user = User.builder()
                .username(userDTO.getUsername())
                .password(encodedPassword)
                .phone(userDTO.getPhone())
                .role(userDTO.getRole())
                .department(userDTO.getDepartment())
                .position(userDTO.getPosition())
                .email(userDTO.getEmail())
                .longitude(userDTO.getLongitude())
                .latitude(userDTO.getLatitude())
                .address(userDTO.getAddress())
                .build();

        // UserDTO에서 받은 역할을 Role 엔티티로 변환

        userRepository.save(user);
    }

    public List<UserDto> getUserList() {
        List<User> userList = userRepository.findAll();
        return userList.stream()
                .map(UserDto::new)  // User -> UserDto 변환
                .collect(Collectors.toList());
    }

    public UserDto findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return new UserDto(user);
    }
}
