package com.erp.dto;

import com.erp.entity.Study;
import com.erp.entity.StudyDetail;
import com.erp.entity.User;
import com.erp.enums.StudyStatus;
import com.erp.enums.StudyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudyOutboxPayload {
    private Long studyId;
    private String title;
    private String description;
    private Integer maxMembers;
    private StudyStatus status;
    private StudyType studyType;
    private String locationAddress;
    private Double locationLatitude;
    private Double locationLongitude;
    private Long createdById;
    private String createdByUsername;
    private String goal;
    private String howToProceed;
    private String tools;
    private String rules;
    private String schedule;

    public static StudyOutboxPayload from(Study study, StudyDetail detail, User user) {
        return StudyOutboxPayload.builder()
                .studyId(study.getId())
                .title(study.getTitle())
                .description(study.getDescription())
                .maxMembers(study.getMaxMembers())
                .status(study.getStatus())
                .studyType(study.getStudyType())
                .locationAddress(study.getLocationAddress())
                .locationLatitude(study.getLocationLatitude())
                .locationLongitude(study.getLocationLongitude())
                .createdById(user != null ? user.getId() : null)
                .createdByUsername(user != null ? user.getUsername() : null)
                .goal(detail != null ? detail.getGoal() : null)
                .howToProceed(detail != null ? detail.getHowToProceed() : null)
                .tools(detail != null ? detail.getTools() : null)
                .rules(detail != null ? detail.getRules() : null)
                .schedule(detail != null ? detail.getSchedule() : null)
                .build();
    }
}
