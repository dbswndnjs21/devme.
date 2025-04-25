package com.erp.service;

import com.erp.domain.dto.UserDto;
import com.erp.domain.entity.User;
import com.erp.domain.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.erp.domain.enums.UserStatus.ACTIVE;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public void join(UserDto userDTO) {
        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());

        User user = User.builder()
                .username(userDTO.getUsername())
                .password(encodedPassword)
                .phone(userDTO.getPhone())
                .role(userDTO.getRole())
                .department(userDTO.getDepartment())
                .position(userDTO.getPosition())
                .email(userDTO.getEmail())
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
