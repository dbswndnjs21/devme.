package com.erp.service;

import com.erp.domain.dto.StudyCreateRequestDto;
import com.erp.domain.entity.Study;
import com.erp.domain.entity.StudyDetail;
import com.erp.domain.entity.StudyMember;
import com.erp.domain.entity.User;
import com.erp.domain.repository.StudyDetailRepository;
import com.erp.domain.repository.StudyMemberRepository;
import com.erp.domain.repository.StudyRepository;
import com.erp.domain.repository.UserRepository;
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
                .role(user.getRole())
                .joinedAt(LocalDateTime.now())
                .build();
        studyMemberRepository.save(studyMember);
    }
}
