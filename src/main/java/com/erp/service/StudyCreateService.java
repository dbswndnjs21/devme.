package com.erp.service;

import com.erp.dto.StudyCreateRequestDto;
import com.erp.entity.Study;
import com.erp.entity.StudyDetail;
import com.erp.entity.StudyMember;
import com.erp.entity.User;
import com.erp.enums.StudyStatus;
import com.erp.enums.StudyType;
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

        Double latitude = null;
        Double longitude = null;
        String fullAddress = null;

        // ✅ OFFLINE 스터디만 주소 & 좌표 처리
        if (dto.getStudyType() == StudyType.OFFLINE) {

            String address = dto.getBaseAddress();
            fullAddress = address;

            if (dto.getDetailAddress() != null && !dto.getDetailAddress().isBlank()) {
                fullAddress += " " + dto.getDetailAddress();
            }

            if (address != null && !address.isBlank()) {
                try {
                    Map<String, Double> coordinates = kakaoMapService.getCoordinates(address);
                    if (coordinates != null) {
                        latitude = coordinates.get("latitude");
                        longitude = coordinates.get("longitude");
                    }
                } catch (Exception e) {
                    log.warn("주소를 좌표로 변환하는데 실패했습니다: {}", address, e);
                }
            }
        }

        Study study = Study.builder()
                .title(dto.getName())
                .description(dto.getDescription())
                .maxMembers(dto.getMaxMembers())
                .createdBy(user)
                .status(StudyStatus.INPROGRESS)
                .studyType(dto.getStudyType())
                .locationAddress(fullAddress)
                .locationLatitude(latitude)
                .locationLongitude(longitude)
                .build();

        Study savedStudy = studyRepository.save(study);

        StudyDetail studyDetail = StudyDetail.builder()
                .study(savedStudy)
                .goal(dto.getGoal())
                .howToProceed(dto.getHowToProceed())
                .tools(dto.getTools())
                .rules(dto.getRules())
                .schedule(dto.getSchedule())
                .build();

        studyDetailRepository.save(studyDetail);

        StudyMember studyMember = StudyMember.builder()
                .study(savedStudy)
                .user(user)
                .role("LEADER")
                .joinedAt(LocalDateTime.now())
                .build();

        studyMemberRepository.save(studyMember);

        // ✅ Elasticsearch 인덱싱
        studySearchService.indexStudy(savedStudy);
    }
}
