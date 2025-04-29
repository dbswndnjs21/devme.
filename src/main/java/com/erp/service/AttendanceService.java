package com.erp.service;

import com.erp.domain.entity.Attendance;
import com.erp.domain.entity.Study;
import com.erp.domain.entity.User;
import com.erp.domain.repository.AttendanceRepository;
import com.erp.domain.repository.StudyRepository;
import com.erp.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

        boolean alreadyExists = attendanceRepository.existsByUserAndStudyAndDate(user, study, today);
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
}
