package com.erp.service;

import com.erp.domain.dto.UserDto;
import com.erp.domain.entity.Study;
import com.erp.domain.entity.StudyJoinRequest;
import com.erp.domain.entity.User;
import com.erp.domain.enums.RequestStatus;
import com.erp.domain.repository.StudyJoinRequestRepository;
import com.erp.domain.repository.StudyRepository;
import com.erp.domain.repository.UserRepository;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


}
