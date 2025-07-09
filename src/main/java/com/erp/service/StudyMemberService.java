package com.erp.service;

import com.erp.dto.StudyDto;
import com.erp.entity.Study;
import com.erp.entity.StudyMember;
import com.erp.entity.User;
import com.erp.repository.StudyMemberRepository;
import com.erp.repository.StudyRepository;
import com.erp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyMemberService {

    private final StudyMemberRepository studyMemberRepository;
    private final UserRepository userRepository;
    private final StudyRepository studyRepository;


//    public List<StudyDto> getMyStudyList(String user) {
//
//        User byUsername = userRepository.findByUsername(user);
//        List<StudyMember> studyMemberList = studyMemberRepository.findByUserId(byUsername.getId());
//        // 내가 포함된 studyMember
//
//        List<Long> studyIds = studyMemberList.stream()
//                .map(studyMember -> studyMember.getStudy().getId())
//                .toList();
//
//        List<Study> studies = studyRepository.findAllById(studyIds);
//
//        List<StudyDto> studyDtoList = studies.stream()
//                .map(study -> StudyDto.builder()
//                        .id(study.getId())
//                        .title(study.getTitle())
//                        .description(study.getDescription())
//                        .maxMembers(study.getMaxMembers())
//                        .createdById(study.getCreatedBy().getId())
//                        .createdAt(study.getCreatedAt())
//                        .status(study.getStatus().getDisplayName())
//                        .build())
//                .toList();
//        return studyDtoList;
//    }

    // 로그인한 사용자와 같은 studyMember 테이블에서 전체 데이터
    public List<StudyDto> getMyStudyList(String username) {
        User user = userRepository.findByUsername(username);
        List<StudyMember> studyMemberList = studyMemberRepository.findByUserId(user.getId());

        List<StudyDto> studyDtoList = studyMemberList.stream()
                .map(studyMember -> {
                    Study study = studyMember.getStudy();
                    return StudyDto.builder()
                            .id(study.getId())
                            .title(study.getTitle())
                            .description(study.getDescription())
                            .maxMembers(study.getMaxMembers())
                            .createdById(study.getCreatedBy().getId())
                            .createdAt(study.getCreatedAt())
                            .status(study.getStatus().getDisplayName())
                            .role(studyMember.getRole())
                            .build();
                })
                .toList();

        return studyDtoList;
    }

}
