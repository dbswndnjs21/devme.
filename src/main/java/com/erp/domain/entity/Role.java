package com.erp.domain.entity;

import com.erp.domain.enums.RoleType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "roles")
public class Role {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    @Enumerated(EnumType.STRING)
    private RoleType name;  // 역할 이름 (예: ADMIN, USER)
}
