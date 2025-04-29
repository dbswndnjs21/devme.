package com.erp.domain.repository;

import com.erp.domain.entity.Study;
import com.erp.domain.entity.StudyMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyMemberRepository extends JpaRepository<StudyMember, Long> {

    List<StudyMember> findByUserId(Long user_id);

    List<StudyMember> findByStudy(Study study);
}
