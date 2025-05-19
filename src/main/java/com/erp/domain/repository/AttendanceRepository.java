package com.erp.domain.repository;

import com.erp.domain.entity.Attendance;
import com.erp.domain.entity.Study;
import com.erp.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    boolean existsByUserAndStudyAndDate(User user, Study study, LocalDate date);
    boolean existsByUserIdAndStudyIdAndDate(Long userId, Long studyId, LocalDate date);
}
