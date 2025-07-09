package com.erp.dto;


import com.erp.entity.Study;
import com.erp.entity.StudyDetail;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class StudyDetailsDto {
    private Study study;
    private StudyDetail studyDetail;
}
