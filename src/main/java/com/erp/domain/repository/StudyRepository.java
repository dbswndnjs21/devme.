package com.erp.domain.repository;

import com.erp.domain.entity.Study;
import com.erp.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyRepository extends JpaRepository<Study, Long> {

    List<Study> findByCreatedBy(User CreatedBy);

}
