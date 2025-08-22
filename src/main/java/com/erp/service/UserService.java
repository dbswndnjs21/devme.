package com.erp.service;

import com.erp.dto.UserDto;
import com.erp.dto.UserProfileRequestDto;
import com.erp.dto.UserProfileResponseDto;
import com.erp.entity.User;
import com.erp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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

    public UserProfileResponseDto getMyProfile(String username) {
        User user = Optional.ofNullable(userRepository.findByUsername(username))
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        return UserProfileResponseDto.builder()
                .username(user.getUsername())
                .phone(user.getPhone())
                .email(user.getEmail())
                .department(user.getDepartment())
                .position(user.getPosition())
                .address(user.getAddress())
                .latitude(user.getLatitude())
                .longitude(user.getLongitude())
                .build();
    }

    /**
     * 내 프로필 수정
     */
    @Transactional
    public void updateProfile(String username, UserProfileRequestDto requestDto) {
        User user = Optional.ofNullable(userRepository.findByUsername(username))
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 전화번호는 필수값이므로 null 체크
        if (requestDto.getPhone() == null || requestDto.getPhone().trim().isEmpty()) {
            throw new RuntimeException("전화번호는 필수 입력값입니다.");
        }

        // 업데이트할 필드들 설정
        user.updateProfile(
                requestDto.getPhone().trim(),
                requestDto.getEmail() != null ? requestDto.getEmail().trim() : null,
                requestDto.getDepartment() != null ? requestDto.getDepartment().trim() : null,
                requestDto.getPosition() != null ? requestDto.getPosition().trim() : null,
                requestDto.getAddress() != null ? requestDto.getAddress().trim() : null
        );

        // 위도, 경도 정보 업데이트
        if (requestDto.getLatitude() != null && requestDto.getLongitude() != null) {
            user.updateLocation(requestDto.getLatitude(), requestDto.getLongitude());
        }

    }
}
