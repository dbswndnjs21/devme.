package com.erp.repository;


import com.erp.entity.Study;
import com.erp.entity.StudyDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudyDetailRepository extends JpaRepository<StudyDetail, Long> {

    Optional<StudyDetail> findByStudy(Study study);
}
