package com.erp.service;

import com.erp.domain.dto.StudyDetailsDto;
import com.erp.domain.entity.Study;
import com.erp.domain.entity.StudyDetail;
import com.erp.domain.repository.StudyDetailRepository;
import com.erp.domain.repository.StudyRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class StudyService {

    private final StudyRepository studyRepository;
    private final StudyDetailRepository studyDetailRepository;

    public StudyDetailsDto getStudyDetails(Long studyId) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new EntityNotFoundException("스터디를 찾을 수 없습니다. ID: " + studyId));

        StudyDetail studyDetail = studyDetailRepository.findByStudy(study)
                .orElseThrow(() -> new EntityNotFoundException("스터디 상세 정보를 찾을 수 없습니다. 스터디 ID: " + studyId));

        return new StudyDetailsDto(study, studyDetail);

    }
}
