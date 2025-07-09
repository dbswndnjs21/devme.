package com.erp.repository;

import com.erp.entity.Study;
import com.erp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyRepository extends JpaRepository<Study, Long> {

    List<Study> findByCreatedBy(User CreatedBy);

}
