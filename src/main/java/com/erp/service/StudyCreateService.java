package com.erp.service;

import com.erp.dto.StudyCreateRequestDto;
import com.erp.entity.Study;
import com.erp.entity.StudyDetail;
import com.erp.entity.StudyMember;
import com.erp.entity.User;
import com.erp.enums.StudyStatus;
import com.erp.repository.StudyDetailRepository;
import com.erp.repository.StudyMemberRepository;
import com.erp.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StudyCreateService {

    private final StudyRepository studyRepository;
    private final StudyDetailRepository studyDetailRepository;
    private final StudyMemberRepository studyMemberRepository;

    @Transactional
    public void studyCreate(StudyCreateRequestDto dto, User user) {
        Study study = Study.builder()
                .title(dto.getName())
                .description(dto.getDescription())
                .maxMembers(dto.getMaxMembers())
                .createdBy(user)
                .status(StudyStatus.INPROGRESS)
                .build();

        studyRepository.save(study);

        StudyDetail studyDetail = StudyDetail.builder()
                .study(study)
                .goal(dto.getGoal())
                .howToProceed(dto.getHowToProceed())
                .tools(dto.getTools())
                .rules(dto.getRules())
                .schedule(dto.getSchedule())
                .build();

        studyDetailRepository.save(studyDetail);

        StudyMember studyMember = StudyMember.builder()
                .study(study)
                .user(user)
                .role("LEADER")
                .joinedAt(LocalDateTime.now())
                .build();
        studyMemberRepository.save(studyMember);
    }
}
