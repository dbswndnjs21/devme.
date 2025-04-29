package com.erp.domain.dto;

import com.erp.domain.entity.Study;
import com.erp.domain.entity.StudyDetail;
import com.erp.domain.entity.StudyMember;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class StudyMainDto {
    private Study study;
    private StudyDetail studyDetail;
    private List<StudyMember> studyMember;
    private boolean isLeader;
    private Long loginUserId;
}
