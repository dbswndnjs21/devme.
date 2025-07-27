package com.erp.service;

import com.erp.dto.ScheduleCreateRequest;
import com.erp.dto.ScheduleDto;
import com.erp.dto.ScheduleResponseDTO;
import com.erp.entity.Schedule;
import com.erp.entity.Study;
import com.erp.entity.User;
import com.erp.repository.ScheduleRepository;
import com.erp.repository.StudyRepository;
import com.erp.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final StudyRepository studyRepository;
    private final UserRepository userRepository;

    public List<ScheduleDto> getSchedulesByStudyId(Long studyId) {
        List<Schedule> schedules = scheduleRepository.findByStudyId(studyId);
        return schedules.stream()
                .map(ScheduleDto::fromEntity)
                .collect(Collectors.toList());
    }

    public void createSchedule(Long studyId, ScheduleCreateRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new EntityNotFoundException("해당 스터디가 존재하지 않습니다."));

        if (!study.getCreatedBy().getId().equals(userId)) {
            throw new AccessDeniedException("스터디 리더만 일정 등록이 가능합니다.");
        }


        Schedule schedule = Schedule.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .date(request.getDate())
                .study(study)
                .createdBy(user)
                .build();

        scheduleRepository.save(schedule);
    }

    public List<ScheduleResponseDTO> getSchedulesByUser(Long userId) {
        List<Schedule> scheduleList = scheduleRepository.findByCreatedById(userId);
        return scheduleList.stream()
                .map(ScheduleResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

}
