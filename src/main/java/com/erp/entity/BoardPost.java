package com.erp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class BoardPost {

    @Id @GeneratedValue
    private Long id;

    private String title;

    @Lob
    private String content;

    @ManyToOne
    private Study study;

    @ManyToOne
    private User user;

    @CreatedDate
    private LocalDateTime createdAt;
}
