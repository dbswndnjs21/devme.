package com.erp.domain.repository;

import com.erp.domain.entity.Study;
import com.erp.domain.entity.StudyJoinRequest;
import com.erp.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyJoinRequestRepository extends JpaRepository<StudyJoinRequest, Long> {

    Boolean existsByStudyAndUser(Study study, User user);

    List<StudyJoinRequest> findByStudyIn(List<Study> studies);
}
