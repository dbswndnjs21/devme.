package com.erp.service;

import com.erp.domain.entity.Study;
import com.erp.domain.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyListService {

    private final StudyRepository studyRepository;

    public List<Study> getStudyList() {
        return studyRepository.findAll();
    }

}
