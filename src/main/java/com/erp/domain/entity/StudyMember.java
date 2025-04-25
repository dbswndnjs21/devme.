package com.erp.domain.entity;

import com.erp.domain.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class StudyMember {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Study study;

    @Enumerated(EnumType.STRING)
    private Role role; // 스터디장 / 멤버

    private LocalDateTime joinedAt;

}
