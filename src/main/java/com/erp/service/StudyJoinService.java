package com.erp.service;

import com.erp.domain.dto.NotificationMessage;
import com.erp.domain.dto.StudyJoinRequestListDto;
import com.erp.domain.dto.UserDto;
import com.erp.domain.entity.Study;
import com.erp.domain.entity.StudyJoinRequest;
import com.erp.domain.entity.StudyMember;
import com.erp.domain.entity.User;
import com.erp.domain.enums.RequestStatus;
import com.erp.domain.repository.StudyJoinRequestRepository;
import com.erp.domain.repository.StudyMemberRepository;
import com.erp.domain.repository.StudyRepository;
import com.erp.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyJoinService {

    private final StudyRepository studyRepository;
    private final StudyJoinRequestRepository studyJoinRequestRepository;
    private final UserRepository userRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final RabbitTemplate rabbitTemplate;


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

        // 🔔 알림 메시지 전송 (스터디장에게)
        String content = String.format("%s님이 '%s' 스터디에 참여 신청했습니다.",
                user.getUsername(), study.getTitle());

        NotificationMessage notification = new NotificationMessage(
                study.getCreatedBy().getId(), content
        );

//        rabbitTemplate.convertAndSend("notification.exchange", "notification.key", notification);

        try {
            rabbitTemplate.convertAndSend("notification.exchange", "notification.key", notification);
            System.out.println("RabbitMQ 메시지 발행 성공!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("RabbitMQ 메시지 발행 실패!");
        }


        return "스터디 신청이 완료되었습니다.";  // 성공 메시지 반환
    }

    public List<StudyJoinRequestListDto> getStudyJoinRequestList(String userName) {
        User user = userRepository.findByUsername(userName);
        // 내가 만든 스터디 목록 찾기
        List<Study> myStudyList = studyRepository.findByCreatedBy(user);
        // 내가만든 목록을 토대로 신청자 테이블에서 조회한 값
        List<StudyJoinRequest> byStudyIn = studyJoinRequestRepository.findByStudyIn(myStudyList);
         // 현재는 모든 상태를 다들고옴

        return byStudyIn.stream()
                .filter(request -> request.getStatus() == RequestStatus.PENDING)
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

    @Transactional
    public void approveRequest(Long requestId) {
        StudyJoinRequest request = studyJoinRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 요청입니다."));

        // StudyMember에 추가
        StudyMember member = StudyMember.builder()
                .study(request.getStudy())
                .user(request.getUser())
                .role("MEMBER") // 스티디원으로 추가
                .joinedAt(LocalDateTime.now())
                .build();

        studyMemberRepository.save(member);

        // 요청 상태 업데이트
        request.updateStatus(RequestStatus.APPROVED);
    }

    @Transactional
    public void rejectRequest(Long requestId) {
        StudyJoinRequest request = studyJoinRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 요청입니다."));

        request.updateStatus(RequestStatus.REJECTED);
    }

}






