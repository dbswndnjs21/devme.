package com.erp.service;

import com.erp.domain.dto.StudyJoinRequestListDto;
import com.erp.domain.dto.UserDto;
import com.erp.domain.entity.Study;
import com.erp.domain.entity.StudyJoinRequest;
import com.erp.domain.entity.User;
import com.erp.domain.enums.RequestStatus;
import com.erp.domain.repository.StudyJoinRequestRepository;
import com.erp.domain.repository.StudyRepository;
import com.erp.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyJoinService {

    private final StudyRepository studyRepository;
    private final StudyJoinRequestRepository studyJoinRequestRepository;
    private final UserRepository userRepository;


    @Transactional
    public String requestJoinStudy(Long studyId, UserDto userDto) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new RuntimeException("스터디 없음"));

        String username = userDto.getUsername();
        User user = userRepository.findByUsername(username);

        if (studyJoinRequestRepository.existsByStudyAndUser(study, user)) {
            return "이미 신청한 스터디입니다.";  // 에러 메시지 반환
        }

        StudyJoinRequest request = StudyJoinRequest.builder()
                .study(study)
                .user(user)
                .status(RequestStatus.PENDING)
                .build();

        studyJoinRequestRepository.save(request);
        return "스터디 신청이 완료되었습니다.";  // 성공 메시지 반환
    }

    public List<StudyJoinRequestListDto> getStudyJoinRequestList(String userName) {
        User user = userRepository.findByUsername(userName);
        // 내가 만든 스터디 목록 찾기
        List<Study> myStudyList = studyRepository.findByCreatedBy(user);
        // 내가만든 목록을 토대로 신청자 테이블에서 조회한 값
        List<StudyJoinRequest> byStudyIn = studyJoinRequestRepository.findByStudyIn(myStudyList);

        //
        return byStudyIn.stream()
                .map(request -> StudyJoinRequestListDto.builder()
                        .requestId(request.getId())
                        .createdAt(request.getCreatedAt())
                        .status(request.getStatus())
                        .studyId(request.getStudy().getId())
                        .userId(request.getUser().getId())
                        .userName(request.getUser().getUsername())
                        .studyTitle(request.getStudy().getTitle())
                        .build())
                .collect(Collectors.toList());
    }




}
