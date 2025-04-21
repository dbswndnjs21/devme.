package com.erp.domain.entity;

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
public class BoardComment {

    @Id @GeneratedValue
    private Long id;

    @Lob
    private String content;

    @ManyToOne
    private BoardPost post;

    @ManyToOne
    private User user;

    @CreatedDate
    private LocalDateTime createdAt;
}
