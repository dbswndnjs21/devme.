package com.erp.entity;

import com.erp.enums.StudyStatus;
import com.erp.enums.StudyType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder
public class Study {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    private String description;

    @Column(nullable = false)
    private int maxMembers;

    @Enumerated(EnumType.STRING)
    private StudyStatus status;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @CreatedDate
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "study", cascade = CascadeType.ALL)
    private StudyDetail detail;

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<StudyJoinRequest> joinRequests = new ArrayList<>();

    @OneToMany(mappedBy = "study")
    @JsonManagedReference
    private List<Attendance> attendances = new ArrayList<>();

    /**
     * 스터디 모임 장소 주소 (오프라인 모임일 경우)
     * 온라인 모임일 땐 null 또는 빈 문자열 가능
     */
    private String locationAddress;

    /**
     * 스터디 모임 장소 위도 (latitude)
     */
    private Double locationLatitude;

    /**
     * 스터디 모임 장소 경도 (longitude)
     */
    private Double locationLongitude;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StudyType studyType;

}
