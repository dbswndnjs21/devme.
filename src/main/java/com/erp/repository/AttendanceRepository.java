package com.erp.repository;

import com.erp.entity.Attendance;
import com.erp.entity.Study;
import com.erp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    boolean existsByUserAndStudyAndDate(User user, Study study, LocalDate date);
    boolean existsByUserIdAndStudyIdAndDate(Long userId, Long studyId, LocalDate date);
}
