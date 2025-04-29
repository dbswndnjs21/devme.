package com.erp.service;

import com.erp.domain.dto.StudyDetailsDto;
import com.erp.domain.dto.StudyMainDto;
import com.erp.domain.entity.Study;
import com.erp.domain.entity.StudyDetail;
import com.erp.domain.entity.StudyMember;
import com.erp.domain.repository.StudyDetailRepository;
import com.erp.domain.repository.StudyMemberRepository;
import com.erp.domain.repository.StudyRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class StudyService {

    private final StudyRepository studyRepository;
    private final StudyDetailRepository studyDetailRepository;
    private final StudyMemberRepository studyMemberRepository;


    // 스터디 id로 studyDetail과 study정보를 한번에 가져옴
    public StudyDetailsDto getStudyDetails(Long studyId) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new EntityNotFoundException("스터디를 찾을 수 없습니다. ID: " + studyId));

        StudyDetail studyDetail = studyDetailRepository.findByStudy(study)
                .orElseThrow(() -> new EntityNotFoundException("스터디 상세 정보를 찾을 수 없습니다. 스터디 ID: " + studyId));

        return new StudyDetailsDto(study, studyDetail);
    }

    public StudyMainDto getStudyMainInfo(Long studyId, Long id) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new EntityNotFoundException("스터디를 찾을 수 없습니다. ID: " + studyId));

        // 로그인한 사용자와 만든 사람 (리더) 와 같은지 여부
        boolean isLeader = study.getCreatedBy().getId().equals(id);

        StudyDetail studyDetail = studyDetailRepository.findByStudy(study)
                .orElseThrow(() -> new EntityNotFoundException("스터디 상세 정보를 찾을 수 없습니다. 스터디 ID: " + studyId));

        List<StudyMember> studyMemberList = studyMemberRepository.findByStudy(study);

        return new StudyMainDto(study, studyDetail, studyMemberList, isLeader, id);
    }
}
