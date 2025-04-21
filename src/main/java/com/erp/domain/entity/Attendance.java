package com.erp.domain.entity;

import com.erp.domain.entity.StudyMember;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Attendance {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private StudyMember studyMember;

    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private Status status; // 출석/지각/결석

    private String memo;

    public enum Status {
        PRESENT, LATE, ABSENT
    }
}
