package com.erp.domain.repository;


import com.erp.domain.entity.Study;
import com.erp.domain.entity.StudyDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudyDetailRepository extends JpaRepository<StudyDetail, Long> {

    Optional<StudyDetail> findByStudy(Study study);
}
