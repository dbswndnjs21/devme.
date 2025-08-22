package com.erp.entity;

import com.erp.enums.Role;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username; // 사용자 ID

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phone;

    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private String department;

    private String position;

    @CreatedDate
    private LocalDateTime createdAt;

    /**
     * 사용자 집 주소 (도로명 주소 or 지번 주소 전체)
     * UI 표시용, 검색 참고용
     */
    private String address;

    /**
     * 사용자 집 위치 위도 (latitude)
     */
    private Double latitude;

    /**
     * 사용자 집 위치 경도 (longitude)
     */
    private Double longitude;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<Attendance> attendances = new ArrayList<>();

    /**
     * 프로필 정보 업데이트
     */
    public void updateProfile(String phone, String email, String department, String position, String address) {
        this.phone = phone;
        this.email = email;
        this.department = department;
        this.position = position;
        this.address = address;
    }

    public void updateLocation(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

}
