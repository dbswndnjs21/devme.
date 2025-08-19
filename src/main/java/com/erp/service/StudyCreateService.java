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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudyCreateService {

    private final StudyRepository studyRepository;
    private final StudyDetailRepository studyDetailRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final StudySearchService studySearchService;
    private final KakaoMapService kakaoMapService;

    @Transactional
    public void studyCreate(StudyCreateRequestDto dto, User user) {
        // 주소가 있는 경우 위도/경도 변환
        String address = dto.getBaseAddress();
        String fullAddress = dto.getBaseAddress();
        if (dto.getDetailAddress() != null && !dto.getDetailAddress().isBlank()) {
            fullAddress += " " + dto.getDetailAddress();
        }

        //좌표 변환 (baseAddress만)
        Double latitude = null;
        Double longitude = null;

        if (address != null && !address.trim().isEmpty()) {
            try {
                Map<String, Double> coordinates = kakaoMapService.getCoordinates(address);
                if (coordinates != null) {
                    latitude = coordinates.get("latitude");
                    longitude = coordinates.get("longitude");
                }
            } catch (Exception e) {
                // 위도/경도 변환 실패해도 스터디 생성은 계속 진행
                log.warn("주소를 좌표로 변환하는데 실패했습니다: {}", address, e);
            }
        }

        Study study = Study.builder()
                .title(dto.getName())
                .description(dto.getDescription())
                .maxMembers(dto.getMaxMembers())
                .createdBy(user)
                .status(StudyStatus.INPROGRESS)
                .locationAddress(fullAddress)
                .locationLatitude(latitude)
                .locationLongitude(longitude)
                .build();

        Study savedStudy = studyRepository.save(study);

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

        // Elasticsearch 인덱싱 추가
        studySearchService.indexStudy(savedStudy);
    }
}