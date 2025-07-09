package com.erp.repository;

import com.erp.entity.Study;
import com.erp.entity.StudyJoinRequest;
import com.erp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyJoinRequestRepository extends JpaRepository<StudyJoinRequest, Long> {

    Boolean existsByStudyAndUser(Study study, User user);

    List<StudyJoinRequest> findByStudyIn(List<Study> studies);
}
