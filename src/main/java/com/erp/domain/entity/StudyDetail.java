package com.erp.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class StudyDetail {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JsonIgnore
    private Study study;

    @Lob
    private String goal;

    @Lob
    private String howToProceed;

    @Lob
    private String tools;

    @Lob
    private String rules;

    @Lob
    private String schedule;
}
