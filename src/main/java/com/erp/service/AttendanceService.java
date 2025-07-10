package com.erp.service;

import com.erp.dto.AttendanceCalendarDto;
import com.erp.entity.Attendance;
import com.erp.entity.Study;
import com.erp.entity.User;
import com.erp.repository.AttendanceRepository;
import com.erp.repository.StudyRepository;
import com.erp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudyRepository studyRepository;
    private final UserRepository userRepository;

    public void markAttendance(Long userId, Long studyId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));
        Study study = studyRepository.findById(studyId)
            .orElseThrow(() -> new IllegalArgumentException("스터디 없음"));

        LocalDate today = LocalDate.now();

        boolean alreadyExists = attendanceRepository.existsByUserIdAndStudyIdAndDate(user.getId(), study.getId(), today);
        if (alreadyExists) {
            throw new IllegalStateException("이미 출석 완료된 상태입니다.");
        }

        Attendance attendance = Attendance.builder()
                .user(user)
                .study(study)
                .date(today)
                .attendedAt(LocalDateTime.now())
                .build();
        attendanceRepository.save(attendance);
    }

    public boolean hasAttendedToday(Long userId, Long studyId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("사용자 없음"));
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new IllegalArgumentException("스터디 없음"));
        LocalDate today = LocalDate.now();
        return attendanceRepository.existsByUserAndStudyAndDate(user, study, today);
    }

    public List<AttendanceCalendarDto> getMyAttendance(Long userId) {
        List<Attendance> attendances = attendanceRepository.findByUserId(userId);
        return attendances.stream()
                .map(AttendanceCalendarDto::new)
                .toList();
    }
}
