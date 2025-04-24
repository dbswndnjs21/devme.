package com.erp.domain.dto;


import com.erp.domain.entity.Study;
import com.erp.domain.entity.StudyDetail;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class StudyDetailsDto {
    private Study study;
    private StudyDetail studyDetail;
}
