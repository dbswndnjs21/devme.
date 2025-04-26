package com.erp.domain.entity;

import com.erp.domain.enums.StudyStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

}
