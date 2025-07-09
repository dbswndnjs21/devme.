package com.erp.dto;

import com.erp.entity.Study;
import com.erp.entity.StudyDetail;
import com.erp.entity.StudyMember;
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
