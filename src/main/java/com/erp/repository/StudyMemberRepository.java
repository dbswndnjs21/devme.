package com.erp.repository;

import com.erp.entity.Study;
import com.erp.entity.StudyMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyMemberRepository extends JpaRepository<StudyMember, Long> {

    List<StudyMember> findByUserId(Long user_id);

    List<StudyMember> findByStudy(Study study);
}
