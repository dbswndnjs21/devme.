package com.erp.domain.entity;

import com.erp.domain.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;  // 사용자 ID (로그인용)

    @Column(nullable = false)
    private String password;  // 비밀번호 (BCrypt 등 암호화 필요)

    @Column(nullable = false, unique = true, length = 100)
    private String email;  // 이메일

    @Column(length = 20)
    private String phoneNumber;  // 전화번호

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private UserStatus status;  // 사용자 상태 (활성, 비활성 등)

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();  // 사용자의 역할 (권한)

    @Column(length = 50)
    private String department;  // 부서

    @Column(length = 50)
    private String position;  // 직급
}
